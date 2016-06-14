package ru.sevenkt.reports;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.reports.beans.DeviceDataBean;

public class Helper {
	public static List<DeviceDataBean> mapToDeviceData(List<Measuring> measurings, LocalDate dateFrom, LocalDate dateTo){
		ArrayList<DeviceDataBean> arrayList = new ArrayList<>();
		DeviceDataBean dataBean=new DeviceDataBean();
		Device dev = measurings.get(0).getDevice();
		dataBean.setName(dev.getDeviceName());
		dataBean.setDateFrom(dateFrom.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		dataBean.setSerialNum(dev.getSerialNum());
		dataBean.setTempColdWater(dev.getTempColdWaterSetting()+"");
		dataBean.setDateTo(dateTo.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));		
		arrayList.add(dataBean);
		return arrayList;
		
	}
}
