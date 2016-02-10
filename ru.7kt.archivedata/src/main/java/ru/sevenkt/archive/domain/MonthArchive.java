package ru.sevenkt.archive.domain;

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
		return new MonthRecord(monthRecordData);		
	}
}
