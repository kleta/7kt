package ru.sevenkt.reports;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.reports.pojo.ConsumptionBean;
import ru.sevenkt.reports.pojo.DeviceDataBean;
import ru.sevenkt.reports.pojo.MeterBean;

public class Helper {
	private static final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	public static Collection<DeviceDataBean> mapToDeviceData(List<Measuring> measurings, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		ArrayList<DeviceDataBean> arrayList = new ArrayList<>();
		DeviceDataBean dataBean = new DeviceDataBean();
		Device dev = measurings.get(0).getDevice();
		dataBean.setName(dev.getDeviceName());
		dataBean.setDateFrom(dateFrom.format(formatterDay));
		dataBean.setSerialNum(dev.getSerialNum());
		dataBean.setTempColdWater(dev.getTempColdWaterSetting() + "");
		dataBean.setDateTo(dateTo.format(formatterDay));
		List<Measuring> work = measurings.stream().filter(m->m.getParameter().equals(Parameters.WORK)).collect(Collectors.toList());
		arrayList.add(dataBean);

		return arrayList;

	}

	public static Collection<ConsumptionBean> mapToConsumption(List<Measuring> measurings, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		LocalDateTime ldtFrom = dateFrom.atStartOfDay();
		Double e1, e2, t1, t2, t3, t4, v1, v2, v3, v4, m1, m2, m3, m4, p1, p2;
		e1 = e2 = t1 = t2 = t3 = t4 = v1 = v2 = v3 = v4 = m1 = m2 = m3 = m4 = p1 = p2 = null;
		List<ConsumptionBean> list = new ArrayList<>();
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		while (ldtFrom.isBefore(dateTo.atStartOfDay()) || ldtFrom.equals(dateTo.atStartOfDay())) {
			List<Measuring> val;
			ConsumptionBean cb = new ConsumptionBean();
//			DateTimeFormatter dtf = null;
//			switch (archiveType) {
//			case HOUR:
//				//ldtFrom = ldtFrom.plusHours(1);
//				dtf=formatterHour;
//				break;
//			case DAY:
//				//ldtFrom = ldtFrom.plusDays(1);
//				dtf=formatterDay;
//				break;
//			case MONTH:
//				//ldtFrom = ldtFrom.plusMonths(1).withDayOfMonth(1);
//				dtf=formatterDay;
//				break;
//			}
			List<Measuring> values = groupByDateTime.get(ldtFrom);
			// List<Measuring> prevValues = groupByDateTime.get(prevDt);
			Instant instant = ldtFrom.atZone(ZoneId.systemDefault()).toInstant();
			Date res = Date.from(instant);
			cb.setDate(res);
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			e1 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP1))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			t1 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP2))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			t2 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP3))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			t3 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP4))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			t4 = val.get(0).getValue();

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			v1 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			v2 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			v3 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			v4 = val.get(0).getValue();

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			m1 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			m2 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			m3 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			m4 = val.get(0).getValue();

			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_P1))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			p1 = val.get(0).getValue();
			val = values.stream().filter(m -> m.getParameter().equals(Parameters.AVG_P2))
					.filter(m -> m.getArchiveType().equals(archiveType)).collect(Collectors.toList());
			p2 = val.get(0).getValue();

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
			switch (archiveType) {
			case HOUR:
				ldtFrom = ldtFrom.plusHours(1);
				//dtf=formatterHour;
				break;
			case DAY:
				ldtFrom = ldtFrom.plusDays(1);
				//dtf=formatterDay;
				break;
			case MONTH:
				ldtFrom = ldtFrom.plusMonths(1).withDayOfMonth(1);
				//dtf=formatterDay;
				break;
			}

		}
		return list;
	}

	private static Collection<ConsumptionBean> mockConsumptionData() {
		LocalDate ld = LocalDate.parse("01.01.2016", formatterDay);
		List<ConsumptionBean> list = new ArrayList<>();
		while (ld.isBefore(LocalDate.parse("17.02.2016", formatterDay))) {
			ConsumptionBean cb = new ConsumptionBean();
			Instant instant = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
			Date res = Date.from(instant);
			cb.setDate(res);
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
			ld = ld.plusDays(1);
		}
		return list;
	}

	public static Collection<MeterBean> mapToMeters(List<Measuring> measurings, LocalDate dateFrom, LocalDate dateTo,
			ArchiveTypes archiveType) {
		List<MeterBean> list = new ArrayList<>();
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		List<Measuring> val;

		MeterBean mb = new MeterBean();
		mb.setDate(dateFrom.format(formatterDay));
		List<Measuring> values = groupByDateTime.get(dateFrom.atStartOfDay());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
				.filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
		mb.setE1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2)).collect(Collectors.toList());
		mb.setE2(val.get(0).getValue());

		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1)).collect(Collectors.toList());
		mb.setM1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2)).collect(Collectors.toList());
		mb.setM2(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3)).collect(Collectors.toList());
		mb.setM3(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4)).collect(Collectors.toList());
		mb.setM4(val.get(0).getValue());

		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1)).collect(Collectors.toList());
		mb.setV1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
		mb.setV2(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3)).collect(Collectors.toList());
		mb.setV3(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4)).collect(Collectors.toList());
		mb.setV4(val.get(0).getValue());
		list.add(mb);

		mb = new MeterBean();
		mb.setDate(dateTo.format(formatterDay));
		values = groupByDateTime.get(dateTo.atStartOfDay());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1))
				.filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
		mb.setE1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E2)).collect(Collectors.toList());
		mb.setE2(val.get(0).getValue());

		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M1)).collect(Collectors.toList());
		mb.setM1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M2)).collect(Collectors.toList());
		mb.setM2(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M3)).collect(Collectors.toList());
		mb.setM3(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.M4)).collect(Collectors.toList());
		mb.setM4(val.get(0).getValue());

		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V1)).collect(Collectors.toList());
		mb.setV1(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V2)).collect(Collectors.toList());
		mb.setV2(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V3)).collect(Collectors.toList());
		mb.setV3(val.get(0).getValue());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.V4)).collect(Collectors.toList());
		mb.setV4(val.get(0).getValue());
		list.add(mb);

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
}
