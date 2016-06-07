package ru.sevenkt.reports.beans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DeviceDataSet {

	private static final String dateToStr = "19.02.2016";
	private static final String dateFromStr = "19.01.2016";
	private Iterator<DeviceDataBean> itr;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public List<DeviceDataBean> create() {
		DeviceDataBean deviceData = new DeviceDataBean();
		deviceData.setDateFrom(dateFromStr);
		deviceData.setDateTo(dateToStr);
		deviceData.setName("Тест");
		deviceData.setSerialNum("2456");
		deviceData.setTempColdWater("7");
		deviceData.setWorkHour("1999");

		deviceData.setMeters(createMeters());
		deviceData.setConsumptions(createConsumptions());

		List<DeviceDataBean> list = new ArrayList<DeviceDataBean>();
		list.add(deviceData);
		return list;
	}

	private List<ConsumptionBean> createConsumptions() {
		LocalDate dateFrom = LocalDate.parse(dateFromStr, formatter);
		LocalDate dateTo = LocalDate.parse(dateToStr, formatter);
		List<ConsumptionBean> list = new ArrayList<ConsumptionBean>();
		while (dateFrom.isBefore(dateTo) ) {
			ConsumptionBean con = new ConsumptionBean();
			con.setDate(dateFrom.format(formatter));
			con.setE1(new Random().nextDouble());
			con.setE2(new Random().nextDouble());
			con.setM1(new Random().nextDouble());
			con.setM2(new Random().nextDouble());
			con.setM3(new Random().nextDouble());
			con.setM3(new Random().nextDouble());
			con.setM4(new Random().nextDouble());
			con.setV1(new Random().nextDouble());
			con.setV2(new Random().nextDouble());
			con.setV3(new Random().nextDouble());
			con.setV3(new Random().nextDouble());
			con.setV4(new Random().nextDouble());
			con.setT1(new Random().nextDouble());
			con.setT2(new Random().nextDouble());
			con.setT3(new Random().nextDouble());
			con.setT4(new Random().nextDouble());
			con.setP1(new Random().nextDouble());
			con.setP2(new Random().nextDouble());
			list.add(con);
			dateFrom=dateFrom.plusDays(1);
		}
		return list;
	}

	private List<MeterBean> createMeters() {
		MeterBean meter = new MeterBean();
		LocalDate dateFrom = LocalDate.parse(dateFromStr, formatter);
		LocalDate dateTo = LocalDate.parse(dateToStr, formatter);
		List<MeterBean> list = new ArrayList<MeterBean>();
		meter.setDate(dateFrom.format(formatter));
		Random random100 = new Random(100);
		meter.setE1(random100.nextDouble());
		meter.setE2(random100.nextDouble());
		meter.setM1(random100.nextDouble());
		meter.setM2(random100.nextDouble());
		meter.setM3(random100.nextDouble());
		meter.setM3(random100.nextDouble());
		meter.setM4(random100.nextDouble());
		meter.setV1(random100.nextDouble());
		meter.setV2(random100.nextDouble());
		meter.setV3(random100.nextDouble());
		meter.setV3(random100.nextDouble());
		meter.setV4(random100.nextDouble());
		list.add(meter);
		meter = new MeterBean();
		meter.setDate(dateTo.format(formatter));
		Random random200 = new Random(200);
		meter.setE1(random200.nextDouble());
		meter.setE2(random200.nextDouble());
		meter.setM1(random200.nextDouble());
		meter.setM2(random200.nextDouble());
		meter.setM3(random200.nextDouble());
		meter.setM3(random200.nextDouble());
		meter.setM4(random200.nextDouble());
		meter.setV1(random200.nextDouble());
		meter.setV2(random200.nextDouble());
		meter.setV3(random200.nextDouble());
		meter.setV3(random200.nextDouble());
		meter.setV4(random200.nextDouble());
		list.add(meter);
		return list;
	}

	public void open(Object obj, Map<String, Object> map) {

	}

	public Object next() {
		if (itr == null)
			itr = create().iterator();
		if (itr.hasNext())
			return itr.next();
		return null;
	}

	public void close() {
	}
	public static void main(String... strings){
		
	}
}
