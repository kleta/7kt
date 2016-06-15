package ru.sevenkt.reports;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.reports.pojo.DeviceDataBean;
import ru.sevenkt.reports.pojo.MeterBean;

public class Helper {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static Collection<DeviceDataBean> mapToDeviceData(List<Measuring> measurings, LocalDate dateFrom, LocalDate dateTo,
			ArchiveTypes archiveType) {
		ArrayList<DeviceDataBean> arrayList = new ArrayList<>();
		DeviceDataBean dataBean = new DeviceDataBean();
		Device dev = measurings.get(0).getDevice();
		dataBean.setName(dev.getDeviceName());
		dataBean.setDateFrom(dateFrom.format(formatter));
		dataBean.setSerialNum(dev.getSerialNum());
		dataBean.setTempColdWater(dev.getTempColdWaterSetting() + "");
		dataBean.setDateTo(dateTo.format(formatter));
		dataBean.setConsumptions(new ArrayList<>());
		dataBean.setMeters(new ArrayList<>());
		//dataBean.setWorkHour(dev.g);
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));

		MeterBean meterBeanFrom = new MeterBean();

		List<Measuring> val;
		switch (archiveType) {
		case HOUR:
			
			break;
		case DAY:
			List<Measuring> values = groupByDateTime.get(dateFrom.plusDays(1).atStartOfDay());
			val = values.stream().filter(m->m.getParameter().equals(Parameters.E1)).collect(Collectors.toList());
			meterBeanFrom.setE1(val.get(0).getValue());
			dataBean.getMeters().add(meterBeanFrom);
			break;
			
		case MONTH:
			break;	
		}
		arrayList.add(dataBean);
		
		return arrayList;

	}
}
