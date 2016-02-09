package ru.sevenkt.archive.domain;

@Length(value = 13416)
public class DayArchive {
	private byte[] data;

	public DayArchive(byte[] dayData) {
		this.data = dayData;
	}

	public DayRecord getDayRecord(int month, int day) throws Exception {
		Length annotationLength = DayRecord.class.getAnnotation(Length.class);
		int size = annotationLength.value();
		int mt = month<6?month:month-6;
		int address=(day-1)*size+(mt-1)*31*size;
		
		byte[] dayRecordData = new byte[size];
		for (int i = 0; i < size; i++) {
			dayRecordData[i] = data[address + i];
		}
		return new DayRecord(dayRecordData);
	}
}
