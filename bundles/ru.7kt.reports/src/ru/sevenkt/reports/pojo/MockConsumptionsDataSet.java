package ru.sevenkt.reports.pojo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MockConsumptionsDataSet {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	private Iterator<ConsumptionBean> it;
	
	public void open(Object appContext, Map<String, Object> dataSetParamValues) {
		it=getConsuptions().iterator();	
	}

	
	public Object next() {
		if(it.hasNext())
			return it.next();
		return null;
	}

	
	public void close() {
		// TODO Auto-generated method stub
		
	}
	private List<ConsumptionBean> getConsuptions() {
		LocalDate ld=LocalDate.parse("01.01.2016", formatter);
		List<ConsumptionBean> list=new ArrayList<>();
		while(ld.isBefore(LocalDate.parse("01.02.2016", formatter))){
			ConsumptionBean cb=new ConsumptionBean();
			cb.setDate(ld.format(formatter));
			Random rnd = new Random();
			cb.setE1(rnd.nextDouble()*1000);
			cb.setE2(rnd.nextDouble()*1000);
			cb.setM1(rnd.nextDouble()*1000);
			cb.setM2(rnd.nextDouble()*1000);
			cb.setM3(rnd.nextDouble()*1000);
			cb.setM4(rnd.nextDouble()*1000);
			cb.setT1(rnd.nextDouble()*10);
			cb.setT2(rnd.nextDouble()*10);
			cb.setT3(rnd.nextDouble()*10);
			cb.setT4(rnd.nextDouble()*10);
			cb.setV1(rnd.nextDouble()*100);
			cb.setV2(rnd.nextDouble()*100);
			cb.setV3(rnd.nextDouble()*100);
			cb.setV4(rnd.nextDouble()*100);
			list.add(cb);
			ld=ld.plusDays(1);
		}
		return list;
	}
}
