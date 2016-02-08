package ru.sevenkt.archive.domain;

@Length(value = 10416)
public class DayArchive {
	private byte[] dayData;

	public DayArchive(byte[] dayData) {
		this.dayData = dayData;
	}

	public DayRecord getDayRecord(int month, int day) {
		Length annotationLength = DayRecord.class.getAnnotation(Length.class);
		int size = annotationLength.value();
		int mt = month>6?month:month-6;
		int address=(day-1)*size+(mt-1)*31*size;
		
		byte[] dayRecordData = new byte[size];
		for (int i = 0; i < size; i++) {
			dayRecordData[i] = dayData[address + i];
		}
		return new DayRecord(dayRecordData);
	}
}
