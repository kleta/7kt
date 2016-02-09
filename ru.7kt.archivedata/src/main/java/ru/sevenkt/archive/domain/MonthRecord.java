package ru.sevenkt.archive.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

import ru.sevenkt.archive.utils.DataUtils;

@Length(value=74)
public class MonthRecord {
	
	private byte[] data;
	
	@Address(value=0)
	@Length(value=1)
	private int formulaNum;
	
	@Address(value=1)
	@Length(value=3)
	private int tempColdWaterSetting;
	
	@Address(value=4)
	@Length(value=3)
	private float volumeByImpulsSetting1;

	@Address(value=7)
	@Length(value=3)
	private float volumeByImpulsSetting2;
	
	@Address(value=10)
	@Length(value=3)
	private float volumeByImpulsSetting3;
	
	@Address(value=13)
	@Length(value=3)
	private float volumeByImpulsSetting4;

	@Address(value=16)
	@Length(value=1)
	private int year;
	
	@Address(value=17)
	@Length(value=1)
	private int month;

	@Address(value=18)
	@Length(value=1)
	private int day;

	@Address(value=19)
	@Length(value=1)
	private int hour;

	@Address(value=20)
	@Length(value=4)
	private long workHour;
	
	@Address(value=24)
	@Length(value=4)
	private float volume1;

	@Address(value=28)
	@Length(value=4)
	private float volume2;
	
	@Address(value=32)
	@Length(value=4)
	private float volume3;
	
	@Address(value=36)
	@Length(value=4)
	private float volume4;
	
	@Address(value=40)
	@Length(value=4)
	private float energy1;

	@Address(value=44)
	@Length(value=4)
	private float energy2;
	
	@Address(value=48)
	@Length(value=4)
	private float weight1;

	@Address(value=52)
	@Length(value=4)
	private float weight2;
	
	@Address(value=56)
	@Length(value=4)
	private float weight3;
	
	@Address(value=60)
	@Length(value=4)
	private float weight4;
	
	@Address(value=64)
	@Length(value=1)
	private int errorChannel1;
	
	@Address(value=65)
	@Length(value=1)
	private int errorChannel2;
	
	@Address(value=66)
	@Length(value=2)
	private int timeError1;
	
	@Address(value=68)
	@Length(value=4)
	private int timeError2;
	
	
	
	public MonthRecord(byte[] monthRecordData) throws Exception {
		data=monthRecordData;
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
				if (type.equals(int.class)) {
					int value = DataUtils.getIntValue(bytes);
					field.set(this, value);
				} else if (type.equals(float.class)) {
					float value = DataUtils.getFloat32Value(bytes);
					field.set(this, value);
				} else if (type.equals(long.class)) {
					long value = DataUtils.getLongValue(bytes);
					field.set(this, value);
				}
			}
			
		}
	}
	
	public LocalDate getDate(){
		LocalDate date = LocalDate.of(year+2000, month, day);
		return date;
	}
}
