package ru.sevenkt.reports.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeGenerator.GeneratorClosedException;

public class MockDeviceDataSet {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	int i=0;
	public Object next(){
		if(i>0)
			return null;
		DeviceDataBean device = new DeviceDataBean();
		i++;
		device.setName("tsts");
		device.setConsumptions(getConsuptions());
		device.setDateFrom("01.01.2016");
		device.setDateTo("01.02.2016");
		device.setMeters(getMeters());
		device.setSerialNum("3522");
		device.setTempColdWater("0");
		device.setWorkHour("10");
		return device;	
		
	}
	private List<MeterBean> getMeters() {
		MeterBean mb=new MeterBean();
		mb.setDate(date);
		return null;
	}
	private List<ConsumptionBean> getConsuptions() {
		LocalDate ld
		return null;
	}
}
