package ru.sevenkt.reports.pojo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MockMetersDataSet{

	
	private Iterator<MeterBean> it;


	public void open(Object appContext, Map<String, Object> dataSetParamValues) {
		it=getMeters().iterator();
		
	}

	
	public Object next() {
		if(it.hasNext())
			return it.next();
		return null;
	}

	
	public void close() {
		// TODO Auto-generated method stub
		
	}
	

	private List<MeterBean> getMeters() {
		List<MeterBean> list=new ArrayList<>();
		MeterBean mb=new MeterBean();
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
		mb=new MeterBean();
		mb.setDate("01.02.2016");
		mb.setE1(100.0233+100);
		mb.setE2(100.0123123+100);
		mb.setM1(87.2991+1000.001);
		mb.setM2(87.2991+843.023);
		mb.setM3(87.2991+4902.020);
		mb.setM4(87.2991+9493.019);
		mb.setV1(12312.02110+424234.1233445);
		mb.setV2(12312.02110+23234.0231);
		mb.setV3(12312.02110+23424.232);
		mb.setV4(12312.0211003+2324.232);
		list.add(mb);
		return list;
	}
}
