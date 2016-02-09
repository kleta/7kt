package ru.sevenkt.archive.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import ru.sevenkt.archive.utils.DataUtils;

@Length(28)
public class HourRecord {
	
	private byte[] data;
	
	@Address(value=0)
	@Length(value=1)
	private int day;
	
	@Address(value=1)
	@Length(value=1)
	private int monthYear;
	
	@Address(value=2)
	@Length(value=2)
	private int avgTemp1;
	
	@Address(value=4)
	@Length(value=2)
	private int avgTemp2;
	
	@Address(value=6)
	@Length(value=2)
	private int avgTemp3;
	
	@Address(value=8)
	@Length(value=2)
	private int avgTemp4;

	@Address(value=10)
	@Length(value=1)
	private int avgPressure1;
	
	@Address(value=11)
	@Length(value=1)
	private int avgPressure2;
	
	@Address(value=12)
	@Length(value=2)
	private float volume1;

	@Address(value=14)
	@Length(value=2)
	private float volume2;
	
	@Address(value=16)
	@Length(value=2)
	private float volume3;
	
	@Address(value=18)
	@Length(value=2)
	private float volume4;
	
	@Address(value=20)
	@Length(value=3)
	private float energy1;

	@Address(value=23)
	@Length(value=3)
	private float energy2;
	
	
	@Address(value=26)
	@Length(value=1)
	private int errorChannel1;
	
	@Address(value=27)
	@Length(value=1)
	private int errorChannel2;

	private int hour;

	public HourRecord(byte[] hourRecordData, int hour) throws Exception {
		data=hourRecordData;
		this.hour=hour;
		init();
	}
	private void init() throws Exception {
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Length.class) && field.isAnnotationPresent(Address.class)) {
				int adr = (int) field.getAnnotation(Address.class).value();
				int len = (int) field.getAnnotation(Length.class).value();
				byte[] bytes = Arrays.copyOfRange(data, adr, adr + len);
				Class<?> type = field.getType();
				if(type.equals(int.class)){
					int value=DataUtils.getIntValue(bytes);
					field.set(this, value);
				}
				else 
					if(type.equals(float.class)){
						float value=DataUtils.getFloat24Value(bytes);
						field.set(this, value);
					}
			}
		}
		
	}
	public LocalDateTime getDateTime() {
		HourRecord c = this;
		int year = monthYear >> 4;
		year = year == 15 ? 15 : year + 16;
		int month = monthYear & 0xF;
		LocalDateTime date = LocalDateTime.of(year + 2000, month, day, hour, 0);
		return date;
	}
	
}
