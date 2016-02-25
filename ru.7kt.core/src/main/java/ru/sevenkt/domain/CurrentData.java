package ru.sevenkt.domain;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import lombok.Data;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.utils.DataUtils;

@Length(value = 48)
@Data
public class CurrentData {

	@Address(value = 0)
	@Length(value = 1)
	private int year;

	@Address(value = 1)
	@Length(value = 1)
	private int month;

	@Address(value = 2)
	@Length(value = 1)
	private int day;

	@Address(value = 3)
	@Length(value = 1)
	private int hour;

	@Address(value = 4)
	@Length(value = 4)
	private long workHour;

	@Address(value = 8)
	@Length(value = 4)
	private float volume1;

	@Address(value = 12)
	@Length(value = 4)
	private float volume2;

	@Address(value = 16)
	@Length(value = 4)
	private float volume3;

	@Address(value = 20)
	@Length(value = 4)
	private float volume4;

	@Address(value = 24)
	@Length(value = 4)
	private float energy1;

	@Address(value = 28)
	@Length(value = 4)
	private float energy2;

	@Address(value = 32)
	@Length(value = 4)
	private float weight1;

	@Address(value = 36)
	@Length(value = 4)
	private float weight2;

	@Address(value = 40)
	@Length(value = 4)
	private float weight3;

	@Address(value = 44)
	@Length(value = 4)
	private float weight4;

	private byte[] data;

	public CurrentData(byte[] data) throws Exception {
		this.data = data;
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
	
	public LocalDateTime getCurrentDateTime(){
		return LocalDateTime.of(year+2000, month, day, hour, 0);
	}

}
