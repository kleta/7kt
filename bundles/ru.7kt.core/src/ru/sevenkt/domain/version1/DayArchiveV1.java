package ru.sevenkt.domain.version1;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.DayArchive;

@Length(value = 2232)
public class DayArchiveV1 extends DayArchive{
	public static int MAX_MONTH_COUNT=6;
	
	private byte[] data;

	public DayArchiveV1(byte[] dayData) {
		super(dayData);
		this.data = dayData;
	}

//	public DayRecord getDayRecord(LocalDate requestDate, LocalDateTime archiveCurrentDateTime) throws Exception {
//		if (requestDate.isBefore(archiveCurrentDateTime.minusMonths(MAX_MONTH_COUNT).toLocalDate()))
//			throw new Exception("Глубина дневного архива " +MAX_MONTH_COUNT + " месяцев. Данные запрашиваемые за дату " + requestDate
//					+ " превышают глубину хранения");
//		Length annotationLength = DayRecordV1.class.getAnnotation(Length.class);
//		int size = annotationLength.value();
//		int month = requestDate.getMonthValue();
//		int day = requestDate.getDayOfMonth();
//		
//		int mt = month<=6?month:month-6;
//		int address=(day-1)*size+(mt-1)*31*size;
//		
//		byte[] dayRecordData = new byte[size];
//		for (int i = 0; i < size; i++) {
//			dayRecordData[i] = data[address + i];
//		}
//		DayRecordV1 dr = new DayRecordV1(dayRecordData);
//		if(requestDate.equals(dr.getDate())){
//			dr.setValid(true);
//		}
//		else
//			dr.setValid(false);
//		return dr;
//	}

	@Override
	public long getMaxMonthDeep() {
		return MAX_MONTH_COUNT;
	}
	
}
