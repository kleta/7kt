package ru.sevenkt.archive.domain;

@Length(value = 41664)
public class HourArchive {
	
	private byte[] data;
	
	
	
	public HourArchive(byte[] data) {
		super();
		this.data = data;
	}



	public HourRecord getHourRecord(int month, int day, int hour) throws Exception{
		Length annotationLength = HourRecord.class.getAnnotation(Length.class);
		int size=annotationLength.value();
		
		int address=(day-1)*24*size+hour*size;
		if(month%2==0)
			address+=31*24*size;
		byte[] hourRecordData=new byte[size];
		for (int i = 0; i < size; i++) {
			hourRecordData[i]=data[address+i];
		}
		return new HourRecord(hourRecordData, hour);
	}
}
