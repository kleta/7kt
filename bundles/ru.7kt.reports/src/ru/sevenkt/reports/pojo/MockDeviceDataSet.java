package ru.sevenkt.reports.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.mozilla.javascript.NativeGenerator.GeneratorClosedException;

public class MockDeviceDataSet {
	
	int i=0;
	public Object next(){
		if(i>0)
			return null;
		DeviceDataBean device = new DeviceDataBean();
		i++;
		device.setName("tsts");	
		device.setDateFrom("01.01.2016");
		device.setDateTo("01.02.2016");
		device.setSerialNum("3522");
		device.setTempColdWater("0");
		device.setTotalWorkHour("10");
		device.setErrorFuncTime1(10);
		device.setErrorFuncTime2(10);
		device.setErrorPowerTime1(100);
		device.setErrorPowerTime2(100);
		device.setErrorTempTime1(163);
		device.setErrorTempTime2(163);
		device.setNormalWorkTime1(700);
		device.setWrongWorkTime1(44);
		device.setNormalWorkTime2(700);
		device.setWrongWorkTime2(44);
		return device;	
		
	}
}
