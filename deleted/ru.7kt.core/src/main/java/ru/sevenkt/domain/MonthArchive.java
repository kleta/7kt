package ru.sevenkt.domain;

import java.time.LocalDate;

import ru.sevenkt.annotations.Length;

@Length(value = 2964)
public class MonthArchive {
	
	public static int MAX_YEAR_COUNT=3;
	
	private byte[] data;
	
	
	public MonthArchive(byte[] data) {
		this.data = data;
	}


	public MonthRecord getMonthRecord(int year, int month) throws Exception{
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
		MonthRecord mr = new MonthRecord(monthRecordData);
		LocalDate reqDate=LocalDate.of(year+2000, month, 1);
		if (year == 16)
			System.out.println();
		if(reqDate.equals(mr.getDate())){
			mr.setValid(true);
		}
		else{
			mr.setValid(false);
		}
		return mr;		
	}
}
