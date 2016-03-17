package ru.sevenkt.domain;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.utils.DataUtils;

@Length(value = 48)
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public long getWorkHour() {
		return workHour;
	}

	public void setWorkHour(long workHour) {
		this.workHour = workHour;
	}

	public float getVolume1() {
		return volume1;
	}

	public void setVolume1(float volume1) {
		this.volume1 = volume1;
	}

	public float getVolume2() {
		return volume2;
	}

	public void setVolume2(float volume2) {
		this.volume2 = volume2;
	}

	public float getVolume3() {
		return volume3;
	}

	public void setVolume3(float volume3) {
		this.volume3 = volume3;
	}

	public float getVolume4() {
		return volume4;
	}

	public void setVolume4(float volume4) {
		this.volume4 = volume4;
	}

	public float getEnergy1() {
		return energy1;
	}

	public void setEnergy1(float energy1) {
		this.energy1 = energy1;
	}

	public float getEnergy2() {
		return energy2;
	}

	public void setEnergy2(float energy2) {
		this.energy2 = energy2;
	}

	public float getWeight1() {
		return weight1;
	}

	public void setWeight1(float weight1) {
		this.weight1 = weight1;
	}

	public float getWeight2() {
		return weight2;
	}

	public void setWeight2(float weight2) {
		this.weight2 = weight2;
	}

	public float getWeight3() {
		return weight3;
	}

	public void setWeight3(float weight3) {
		this.weight3 = weight3;
	}

	public float getWeight4() {
		return weight4;
	}

	public void setWeight4(float weight4) {
		this.weight4 = weight4;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
