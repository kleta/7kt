package ru.sevenkt.reports;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
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
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static Collection<DeviceDataBean> mapToDeviceData(List<Measuring> measurings, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		ArrayList<DeviceDataBean> arrayList = new ArrayList<>();
		DeviceDataBean dataBean = new DeviceDataBean();
		Device dev = measurings.get(0).getDevice();
		dataBean.setName(dev.getDeviceName());
		dataBean.setDateFrom(dateFrom.format(formatter));
		dataBean.setSerialNum(dev.getSerialNum());
		dataBean.setTempColdWater(dev.getTempColdWaterSetting() + "");
		dataBean.setDateTo(dateTo.format(formatter));
		// dataBean.setConsumptions(new ArrayList<>());
		// dataBean.setMeters(new ArrayList<>());
		// dataBean.setWorkHour(dev.g);

		arrayList.add(dataBean);

		return arrayList;

	}

	public static Collection<ConsumptionBean> mapToConsumption(List<Measuring> measurings, LocalDate dateFrom,
			LocalDate dateTo, ArchiveTypes archiveType) {
		LocalDate ld = LocalDate.parse("01.01.2016", formatter);
		List<ConsumptionBean> list = new ArrayList<>();
		while (ld.isBefore(LocalDate.parse("01.02.2016", formatter))) {
			ConsumptionBean cb = new ConsumptionBean();
			cb.setDate(ld.format(formatter));
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
		mb.setDate(dateFrom.plusDays(1).format(formatter));
		List<Measuring> values = groupByDateTime.get(dateFrom.plusDays(1).atStartOfDay());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1)).filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
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
		mb.setDate(dateTo.format(formatter));
		values = groupByDateTime.get(dateTo.atStartOfDay());
		val = values.stream().filter(m -> m.getParameter().equals(Parameters.E1)).filter(m -> m.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
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
