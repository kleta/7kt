package ru.sevenkt.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ru.sevenkt.annotations.Length;

@Length(value = 13416)
public class DayArchive {
	public static int MAX_MONTH_COUNT=6;
	
	private byte[] data;

	public DayArchive(byte[] dayData) {
		this.data = dayData;
	}

	public DayRecord getDayRecord(LocalDate requestDate, LocalDateTime archiveCurrentDateTime) throws Exception {
		if (requestDate.isBefore(archiveCurrentDateTime.minusMonths(MAX_MONTH_COUNT).toLocalDate()))
			throw new Exception("Глубина дневного архива " +MAX_MONTH_COUNT + " месяцев. Данные запрашиваемые за дату " + requestDate
					+ " превышают глубину хранения");
		Length annotationLength = DayRecord.class.getAnnotation(Length.class);
		int size = annotationLength.value();
		int month = requestDate.getMonthValue();
		int day = requestDate.getDayOfMonth();
		
		int mt = month<6?month:month-6;
		int address=(day-1)*size+(mt-1)*31*size;
		
		byte[] dayRecordData = new byte[size];
		for (int i = 0; i < size; i++) {
			dayRecordData[i] = data[address + i];
		}
		DayRecord dr = new DayRecord(dayRecordData);
		if(requestDate.equals(dr.getDate())){
			dr.setValid(true);
		}
		else
			dr.setValid(false);
		return dr;
	}
	
}
