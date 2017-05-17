package ru.sevenkt.domain;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;

public abstract class MonthArchive {

	private byte[] data;

	private Map<LocalDate, IMonthRecord> records;

	public abstract long getMaxYearsDeep();

	public MonthArchive(byte[] data) {
		this.data = data;
	}

	protected void parseData(Class<? extends IMonthRecord> class1) throws Exception {
		Length annotationLength = class1.getAnnotation(Length.class);
		int size = annotationLength.value();
		records = new HashMap<>();
	//	long monthAddr = class1.getAnnotation(Address.class).value();
		for (int i = 0; i < data.length; i += size) {
			byte[] monthRecordData = Arrays.copyOfRange(data, i, i + size);
			Constructor<? extends IMonthRecord> cons = class1.getConstructor(byte[].class);
			IMonthRecord mr = cons.newInstance(monthRecordData);
			LocalDate date = mr.getDate();
			if (date != null) {
				mr.setValid(true);
				records.put(date, mr);
				System.out.println("adr=" + (i + 300) + " " + date + "=" + mr);
			}
		}
	}

	public IMonthRecord getMonthRecord(LocalDate date) throws Exception {
		return records.get(date);
	}

}
