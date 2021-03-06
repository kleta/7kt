package ru.sevenkt.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.annotations.RecordLength;

public abstract class HourArchive {

	private byte[] data;

	private Map<LocalDateTime, IHourRecord> records;

	public HourArchive(byte[] data) {
		this.data = data;
	}

	public HourArchive(Map<LocalDateTime, IHourRecord> map) {
		records = map;
	}

	public abstract long getMaxDaysDeep();

	public Map<Parameters, BigDecimal> getSumByDay(LocalDate date)
			throws IllegalArgumentException, IllegalAccessException {
		String[] categories = { ParametersConst.ENERGY, ParametersConst.VOLUME, ParametersConst.WEIGHT };
		Map<Parameters, BigDecimal> map = new HashMap<>();
		for (LocalDateTime dateTime = date.atTime(1, 0); dateTime
				.isBefore(date.plusDays(1).atTime(1, 0)); dateTime = dateTime.plusHours(1)) {
			IHourRecord r = records.get(dateTime);
			if (r != null) {
				Field[] fields = r.getClass().getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Parameter.class)) {
						Parameters param = field.getAnnotation(Parameter.class).value();
						if (Stream.of(categories).anyMatch(c -> c.equals(param.getCategory()))) {
							field.setAccessible(true);
							
							Object val = field.get(r);
						//	System.out.println(val);
							BigDecimal bdVal = new BigDecimal(val + "");

							BigDecimal bd = map.get(param);
							if (bd == null)
								map.put(param, bdVal);
							else
								map.put(param, bd.add(bdVal));
						}

					}
				}
			}
		}
		return map;
	}

	protected void parseData(Class<? extends IHourRecord> class1) throws Exception {
		RecordLength annotationRecordLength = getClass().getAnnotation(RecordLength.class);
		int size = annotationRecordLength.value();
		records = new HashMap<>();
		int hour = 0;
		for (int i = 0; i < data.length; i += size) {
			byte[] hourRecordData = Arrays.copyOfRange(data, i, i + size);
			Constructor<? extends IHourRecord> cons = class1.getConstructor(byte[].class, int.class);
			IHourRecord hr = cons.newInstance(hourRecordData, hour);
			LocalDateTime date = hr.getDateTime();
			if (date != null) {
				hr.setValid(true);
				records.put(date, hr);
				System.out.println("addr="+i+" "+hr);
			}
			hour++;
			if (hour == 24)
				hour = 0;
		}
	}

	public List<IHourRecord> getRecordsByDay(LocalDate date) {
		List<IHourRecord> records = new ArrayList<>();
		for (LocalDateTime dateTime = date.atTime(1, 0); dateTime
				.isBefore(date.plusDays(1).atTime(1, 0)); dateTime = dateTime.plusHours(1)) {
			records.add(this.records.get(dateTime));
		}
		return records;
	}

}
