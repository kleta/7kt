package ru.sevenkt.domain;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.version3.DayRecordV3;

public abstract class DayArchive {

	private byte[] data;

	private Map<LocalDate, IDayRecord> records;

	protected DayArchive(byte[] data) {
		this.data = data;
	}

	public IDayRecord getDayRecord(LocalDate day) {
		return records.get(day);
	}

	public abstract long getMaxMonthDeep();

	protected void parseData(Class<? extends IDayRecord> class1) throws Exception {
		Length annotationLength = class1.getAnnotation(Length.class);
		int size = annotationLength.value();
		records = new HashMap<>();
		for (int i = 0; i < data.length; i += size) {
			byte[] dayRecordData = Arrays.copyOfRange(data, i, i + size);
			// DayRecordV3 dr = new DayRecordV3(dayRecordData);
			Constructor<? extends IDayRecord> cons = class1.getConstructor(byte[].class);
			IDayRecord dr = cons.newInstance(dayRecordData);
			LocalDate date = dr.getDate();
			if (date != null) {
				dr.setPrevDayRecord(records.get(date.minusDays(1)));
				records.put(date, dr);
				System.out.println("adr=" + (i + 3000) + " " + date + "=" + dr);
			}
		}
	}

}
