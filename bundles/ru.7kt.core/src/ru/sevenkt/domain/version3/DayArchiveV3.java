package ru.sevenkt.domain.version3;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.IDayRecord;

@Length(value = 10416)
public class DayArchiveV3 extends DayArchive{
	public static int MAX_MONTH_COUNT=6;
	

	public DayArchiveV3(byte[] dayData) throws Exception {
		super(dayData);
		parseData(DayRecordV3.class);
	}


//	public DayRecord getDayRecord(LocalDate requestDate, LocalDateTime archiveCurrentDateTime) throws Exception {
//		if (requestDate.isBefore(archiveCurrentDateTime.minusMonths(MAX_MONTH_COUNT).toLocalDate()))
//			throw new Exception("Глубина дневного архива " +MAX_MONTH_COUNT + " месяцев. Данные запрашиваемые за дату " + requestDate
//					+ " превышают глубину хранения");
//		Length annotationLength = DayRecordV3.class.getAnnotation(Length.class);
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
//		DayRecordV3 dr = new DayRecordV3(dayRecordData);
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
