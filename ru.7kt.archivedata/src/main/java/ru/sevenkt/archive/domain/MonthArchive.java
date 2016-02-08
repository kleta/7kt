package ru.sevenkt.archive.domain;

import java.util.HashMap;

@Length(value = 2664)
public class MonthArchive {
	private byte[] data= new byte[2964];
	
	
	public MonthArchive(byte[] data) {
		super();
		this.data = data;
	}


	public MonthRecord getMonthRecord(int year, int month){
		Length annotationLength = MonthRecord.class.getAnnotation(Length.class);
		int size=annotationLength.value();
		int yt = year-15;
		while(yt>2)
			yt-=3;
		int address = (month-1)* size+yt*12*size;
		
		
		byte[] monthRecordData=new byte[size];
		for (int i = 0; i < size; i++) {
			monthRecordData[i]=data[address+i];
		}
		return new MonthRecord(monthRecordData);
		
	}
}
