package ru.sevenkt.reports.pojo;

import java.util.List;

public class DeviceDataBean {
	
	private String name;
	
	private String serialNum;
	
	private String dateFrom;
	
	private String dateTo;
	
	private List<MeterBean> meters;
	
	private List<ConsumptionBean> consumptions;
	
	private String  tempColdWater;
	
	private String workHour;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public List<MeterBean> getMeters() {
		return meters;
	}

	public void setMeters(List<MeterBean> meters) {
		this.meters = meters;
	}

	public List<ConsumptionBean> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(List<ConsumptionBean> consumptions) {
		this.consumptions = consumptions;
	}

	public String getTempColdWater() {
		return tempColdWater;
	}

	public void setTempColdWater(String tempColdWater) {
		this.tempColdWater = tempColdWater;
	}

	public String getWorkHour() {
		return workHour;
	}

	public void setWorkHour(String workHour) {
		this.workHour = workHour;
	}

	
	
	

}
