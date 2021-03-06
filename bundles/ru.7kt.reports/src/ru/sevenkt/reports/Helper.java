package ru.sevenkt.reports;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;
import ru.sevenkt.reports.pojo.ConsumptionBean;
import ru.sevenkt.reports.pojo.DeviceDataBean;
import ru.sevenkt.reports.pojo.MeterBean;
import ru.sevenkt.reports.services.IReportService;

public class Helper {
	private static final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	public static Collection<DeviceDataBean> mapToDeviceData(Map<String, Object> map, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		ArrayList<DeviceDataBean> arrayList = new ArrayList<>();
		DeviceDataBean dataBean = new DeviceDataBean();
		Device dev = (Device) map.get(IReportService.DEVICE);
		dataBean.setName(dev.getDeviceName());
		dataBean.setDateFrom(dateFrom.format(formatterDay));
		dataBean.setSerialNum(dev.getSerialNum());
		dataBean.setTempColdWater(new Float(dev.getTempColdWaterSetting()).intValue() + "");
		dataBean.setDateTo(dateTo.format(formatterDay));
		List<Measuring> ms = (List<Measuring>) map.get(IReportService.MONTH_MEASURINGS);
		ms.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
		ms = ms.stream().filter(m -> m.getParameter().equals(Parameters.WORK)).collect(Collectors.toList());
		if (!ms.isEmpty()) {
			Measuring m = ms.get(ms.size() - 1);
			long hours = ChronoUnit.HOURS.between(m.getDateTime(), dateTo.atStartOfDay());
			dataBean.setTotalWorkHour(new Integer((int) (m.getValue().intValue() + hours)) + "");
		}
		List<Error> errors = (List<Error>) map.get(IReportService.ERRORS);
		if (errors.isEmpty()) {
			dataBean.setErrorFuncTime1(0);
			dataBean.setErrorFuncTime2(0);
			dataBean.setErrorPowerTime1(0);
			dataBean.setErrorPowerTime2(0);
			dataBean.setErrorTempTime1(0);
			dataBean.setErrorTempTime2(0);
			dataBean.setErrorVolTime1(0);
			dataBean.setErrorVolTime2(0);
		}
		long hours = ChronoUnit.HOURS.between(dateFrom.atStartOfDay(), dateTo.atStartOfDay());
		switch (archiveType) {
		case DAY:
			ms = (List<Measuring>) map.get(IReportService.DAY_MEASURINGS);

			List<Measuring> errorTimes1 = ms.stream().filter(m -> m.getParameter().equals(Parameters.ERROR_TIME1)
					&& m.getDateTime().isAfter(dateFrom.atStartOfDay())).collect(Collectors.toList());
			List<Measuring> errorTimes2 = ms.stream().filter(m -> m.getParameter().equals(Parameters.ERROR_TIME2)
					&& m.getDateTime().isAfter(dateFrom.atStartOfDay())).collect(Collectors.toList());
			int et1 = errorTimes1.stream().mapToInt(m -> m.getValue().intValue()).sum();
			int et2 = errorTimes2.stream().mapToInt(m -> m.getValue().intValue()).sum();
			dataBean.setWrongWorkTime1(et1);
			dataBean.setWrongWorkTime2(et2);
			dataBean.setNormalWorkTime1((int) (hours - et1));
			dataBean.setNormalWorkTime2((int) (hours - et2));
			List<Error> dayErrors = errors.stream().filter(e -> e.getArchiveType().equals(ArchiveTypes.DAY))
					.collect(Collectors.toList());
			dataBean.setErrorFuncTime1(getErrorTimes(ErrorCodes.E1, dayErrors, errorTimes1));
			dataBean.setErrorFuncTime2(getErrorTimes(ErrorCodes.E2, dayErrors, errorTimes2));
			dataBean.setErrorTempTime1(getErrorTimes(ErrorCodes.T1, dayErrors, errorTimes1));
			dataBean.setErrorTempTime2(getErrorTimes(ErrorCodes.T2, dayErrors, errorTimes2));
			dataBean.setErrorVolTime1(getErrorTimes(ErrorCodes.V1, dayErrors, errorTimes1));
			dataBean.setErrorVolTime2(getErrorTimes(ErrorCodes.V2, dayErrors, errorTimes2));
			dataBean.setErrorPowerTime1(getErrorTimes(ErrorCodes.U, dayErrors, errorTimes1));
			dataBean.setErrorPowerTime2(getErrorTimes(ErrorCodes.U, dayErrors, errorTimes1));
			break;
		case HOUR:
			ms = (List<Measuring>) map.get(IReportService.HOUR_MEASURINGS);
			errorTimes1 = ms.stream().filter(m -> m.getParameter().equals(Parameters.ERROR_TIME1)
					&& m.getDateTime().isAfter(dateFrom.atStartOfDay())).collect(Collectors.toList());
			errorTimes2 = ms.stream().filter(m -> m.getParameter().equals(Parameters.ERROR_TIME2)
					&& m.getDateTime().isAfter(dateFrom.atStartOfDay())).collect(Collectors.toList());
			et1 = errorTimes1.stream().mapToInt(m -> m.getValue().intValue()).sum();
			et2 = errorTimes2.stream().mapToInt(m -> m.getValue().intValue()).sum();
			dataBean.setWrongWorkTime1(et1);
			dataBean.setWrongWorkTime2(et2);
			dataBean.setNormalWorkTime1((int) (hours - et1));
			dataBean.setNormalWorkTime2((int) (hours - et2));
			List<Error> hourErrors = errors.stream().filter(e -> e.getArchiveType().equals(ArchiveTypes.HOUR))
					.collect(Collectors.toList());
			dataBean.setErrorFuncTime1(getErrorTimes(ErrorCodes.E1, hourErrors, errorTimes1));
			dataBean.setErrorFuncTime2(getErrorTimes(ErrorCodes.E2, hourErrors, errorTimes2));
			dataBean.setErrorTempTime1(getErrorTimes(ErrorCodes.T1, hourErrors, errorTimes1));
			dataBean.setErrorTempTime2(getErrorTimes(ErrorCodes.T2, hourErrors, errorTimes2));
			dataBean.setErrorVolTime1(getErrorTimes(ErrorCodes.V1, hourErrors, errorTimes1));
			dataBean.setErrorVolTime2(getErrorTimes(ErrorCodes.V2, hourErrors, errorTimes2));
			dataBean.setErrorPowerTime1(getErrorTimes(ErrorCodes.U, hourErrors, errorTimes1));
			dataBean.setErrorPowerTime2(getErrorTimes(ErrorCodes.U, hourErrors, errorTimes1));
			break;
		}

		arrayList.add(dataBean);

		return arrayList;

	}

	private static Integer getErrorTimes(ErrorCodes e1, List<Error> errors, List<Measuring> errorTimes1) {
		Map<LocalDateTime, List<Measuring>> groupingByDateTime = errorTimes1.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		List<Error> filterErrors = errors.stream().filter(e -> e.getErrorCode().equals(e1))
				.collect(Collectors.toList());
		Integer sum = 0;
		for (Error error : filterErrors) {
			List<Measuring> time = groupingByDateTime.get(error.getDateTime());
			if (time != null && !time.isEmpty())
				sum = sum + time.get(0).getValue().intValue();
		}
		return sum;
	}

	public static Collection<ConsumptionBean> mapToConsumption(Map<String, Object> map, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		LocalDateTime ldtFrom = dateFrom.atStartOfDay();
		Double e1, e2, t1, t2, t3, t4, v1, v2, v3, v4, m1, m2, m3, m4, p1, p2;
		e1 = e2 = t1 = t2 = t3 = t4 = v1 = v2 = v3 = v4 = m1 = m2 = m3 = m4 = p1 = p2 = null;
		List<ConsumptionBean> list = new ArrayList<>();
		Collection<Measuring> measurings = null;
		switch (archiveType) {
		case HOUR:
			measurings = (Collection<Measuring>) map.get(IReportService.HOUR_MEASURINGS);
			ldtFrom = dateFrom.atTime(1, 0);
			break;
		case DAY:
			measurings = (Collection<Measuring>) map.get(IReportService.DAY_MEASURINGS);
			break;
		case MONTH:
			measurings = (Collection<Measuring>) map.get(IReportService.MONTH_MEASURINGS);
			break;
		}

		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		while (ldtFrom.isBefore(dateTo.atStartOfDay()) || ldtFrom.equals(dateTo.atStartOfDay())) {
			List<Measuring> val;
			ConsumptionBean cb = new ConsumptionBean();
			List<Measuring> values = groupByDateTime.get(ldtFrom);
			if (ldtFrom.equals(LocalDateTime.of(2016, 01, 28, 8, 0)))
				System.out.println();
			cb.setDate(Timestamp.valueOf(ldtFrom));
			if (values != null) {
				val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					e1 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					e2 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP1))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					t1 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP2))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					t2 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP3))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					t3 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP4))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					t4 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					v1 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					v2 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					v3 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					v4 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					m1 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					m2 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					m3 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					m4 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_P1))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					p1 = val.get(0).getValue().doubleValue();

				val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_P2))
						.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
				if (!val.isEmpty())
					p2 = val.get(0).getValue().doubleValue();

				cb.setE1(e1);
				cb.setE2(e2);
				cb.setM1(m1);
				cb.setM2(m2);
				cb.setM3(m3);
				cb.setM4(m4);
				cb.setT1(t1);
				cb.setT2(t2);
				cb.setT3(t3);
				cb.setT4(t4);
				cb.setP1(p1);
				cb.setP2(p2);
				cb.setV1(v1);
				cb.setV2(v2);
				cb.setV3(v3);
				cb.setV4(v4);
				list.add(cb);
			}
			switch (archiveType) {
			case HOUR:
				ldtFrom = ldtFrom.plusHours(1);
				break;
			case DAY:
				ldtFrom = ldtFrom.plusDays(1);
				break;
			case MONTH:
				ldtFrom = ldtFrom.plusMonths(1).withDayOfMonth(1);
				break;
			}

		}
		return list;
	}

	private static Collection<ConsumptionBean> mockConsumptionData() {
		LocalDateTime ld = LocalDateTime.parse("01.01.2016 00:00", formatterHour);
		List<ConsumptionBean> list = new ArrayList<>();
		while (ld.isBefore(LocalDateTime.parse("17.02.2016 00:00", formatterHour))) {
			ConsumptionBean cb = new ConsumptionBean();

			cb.setDate(Timestamp.valueOf(ld));
			Random rnd = new Random();
			cb.setE1(rnd.nextDouble() * 1000);
			cb.setE2(rnd.nextDouble() * 1000);
			cb.setM1(rnd.nextDouble() * 1000);
			cb.setM2(rnd.nextDouble() * 1000);
			cb.setM3(rnd.nextDouble() * 1000);
			cb.setM4(rnd.nextDouble() * 1000);
			cb.setT1(rnd.nextDouble() * 10);
			cb.setT2(rnd.nextDouble() * 10);
			cb.setT3(rnd.nextDouble() * 10);
			cb.setT4(rnd.nextDouble() * 10);
			cb.setV1(rnd.nextDouble() * 100);
			cb.setV2(rnd.nextDouble() * 100);
			cb.setV3(rnd.nextDouble() * 100);
			cb.setV4(rnd.nextDouble() * 100);
			list.add(cb);
			ld = ld.plusDays(1).plusHours(1);
		}
		return list;
	}

	public static Collection<MeterBean> mapToMeters(Map<String, Object> map, ArchiveTypes archiveType) {
		List<MeterBean> list = new ArrayList<>();
		Collection<Measuring> measurings = (Collection<Measuring>) map.get(IReportService.DAY_MEASURINGS);
		if (!measurings.isEmpty()) {
			Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
					.collect(Collectors.groupingBy(Measuring::getDateTime));
			List<Measuring> val;
			LocalDateTime minDate = measurings.stream().map(m -> m.getDateTime()).min(LocalDateTime::compareTo).get();
			LocalDateTime maxDate = measurings.stream()
					.filter(m -> !m.getParameter().getCategory().equals(ParametersConst.TIME)).map(m -> m.getDateTime())
					.max(LocalDateTime::compareTo).get();
			MeterBean mb = new MeterBean();
			mb.setDate(minDate.format(formatterDay));
			List<Measuring> values = groupByDateTime.get(minDate);
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
					.filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
			mb.setE1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2)).collect(Collectors.toList());
			mb.setE2(val.get(0).getValue().doubleValue());

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1)).collect(Collectors.toList());
			mb.setM1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2)).collect(Collectors.toList());
			mb.setM2(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3)).collect(Collectors.toList());
			mb.setM3(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4)).collect(Collectors.toList());
			mb.setM4(val.get(0).getValue().doubleValue());

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1)).collect(Collectors.toList());
			mb.setV1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
			mb.setV2(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3)).collect(Collectors.toList());
			mb.setV3(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4)).collect(Collectors.toList());
			mb.setV4(val.get(0).getValue().doubleValue());
			list.add(mb);

			mb = new MeterBean();
			mb.setDate(maxDate.format(formatterDay));
			values = groupByDateTime.get(maxDate);
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
					.filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
			mb.setE1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2)).collect(Collectors.toList());
			mb.setE2(val.get(0).getValue().doubleValue());

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1)).collect(Collectors.toList());
			mb.setM1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2)).collect(Collectors.toList());
			mb.setM2(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3)).collect(Collectors.toList());
			mb.setM3(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4)).collect(Collectors.toList());
			mb.setM4(val.get(0).getValue().doubleValue());

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1)).collect(Collectors.toList());
			mb.setV1(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
			mb.setV2(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3)).collect(Collectors.toList());
			mb.setV3(val.get(0).getValue().doubleValue());
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4)).collect(Collectors.toList());
			mb.setV4(val.get(0).getValue().doubleValue());
			list.add(mb);
		}
		return list;
	}

	private static Collection<MeterBean> mockData() {
		List<MeterBean> list = new ArrayList<>();
		MeterBean mb = new MeterBean();
		mb.setDate("01.01.2016");
		mb.setE1(100.0233);
		mb.setE2(100.0123123);
		mb.setM1(87.2991);
		mb.setM2(87.2991);
		mb.setM3(87.2991);
		mb.setM4(87.2991);
		mb.setV1(12312.02110);
		mb.setV2(12312.02110);
		mb.setV3(12312.02110);
		mb.setV4(12312.02110);
		list.add(mb);
		mb = new MeterBean();
		mb.setDate("01.02.2016");
		mb.setE1(100.0233 + 100);
		mb.setE2(100.0123123 + 100);
		mb.setM1(87.2991 + 1000.001);
		mb.setM2(87.2991 + 843.023);
		mb.setM3(87.2991 + 4902.020);
		mb.setM4(87.2991 + 9493.019);
		mb.setV1(12312.02110 + 424234.1233445);
		mb.setV2(12312.02110 + 23234.0231);
		mb.setV3(12312.02110 + 23424.232);
		mb.setV4(12312.0211003 + 2324.232);
		list.add(mb);
		return list;
	}

	public static Map mapToParameters(Map<String, Object> map) {
		Map<String, Boolean> parameters = new HashMap<>();
		parameters.put("e1", true);
		parameters.put("e2", true);
		parameters.put("t1", true);
		parameters.put("t2", true);
		parameters.put("t3", true);
		parameters.put("t4", true);
		parameters.put("v1", true);
		parameters.put("v2", true);
		parameters.put("v3", true);
		parameters.put("v4", true);
		parameters.put("m1", true);
		parameters.put("m2", true);
		parameters.put("m3", true);
		parameters.put("m4", true);
		parameters.put("p1", true);
		parameters.put("p2", true);
		List<Params> params = (List<Params>) map.get(IReportService.PARAMS);
		for (Params param : params) {
			switch (param.getId()) {
			case E1:
				parameters.put("e1", false);
				break;
			case E2:
				parameters.put("e2", false);
				break;
			case AVG_TEMP1:
				parameters.put("t1", false);
				break;
			case AVG_TEMP2:
				parameters.put("t2", false);
				break;
			case AVG_TEMP3:
				parameters.put("t3", false);
				break;
			case AVG_TEMP4:
				parameters.put("t4", false);
				break;
			case V1:
				parameters.put("v1", false);
				break;
			case V2:
				parameters.put("v2", false);
				break;
			case V3:
				parameters.put("v3", false);
				break;
			case V4:
				parameters.put("v4", false);
				break;
			case M1:
				parameters.put("m1", false);
				break;
			case M2:
				parameters.put("m2", false);
				break;
			case M3:
				parameters.put("m3", false);
				break;
			case M4:
				parameters.put("m4", false);
				break;
			case AVG_P1:
				parameters.put("p1", false);
				break;
			case AVG_P2:
				parameters.put("p2", false);
				break;
			}

		}
		return parameters;
	}
}
