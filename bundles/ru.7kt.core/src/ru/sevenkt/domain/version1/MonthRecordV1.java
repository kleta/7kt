package ru.sevenkt.domain.version1;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.domain.IMonthRecord;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.utils.DataUtils;


public class MonthRecordV1 implements IMonthRecord{
	
	private byte[] data;
		
	@Address(value=0)
	@Length(value=1)
	private int monthYear;
	
	@Address(value=1)
	@Length(value=4)
	@Parameter(Parameters.V1)
	private float volume1;

	@Address(value=5)
	@Length(value=4)
	@Parameter(Parameters.V2)
	private float volume2;
	
	@Address(value=9)
	@Length(value=4)
	@Parameter(Parameters.V3)
	private float volume3;
	
	@Address(value=13)
	@Length(value=4)
	@Parameter(Parameters.V4)
	private float volume4;
	
	@Address(value=17)
	@Length(value=4)
	@Parameter(Parameters.E1)
	private float energy1;

	@Address(value=21)
	@Length(value=4)
	@Parameter(Parameters.E2)
	private float energy2;
	
	private boolean valid;
	
	public MonthRecordV1(byte[] monthRecordData) throws Exception {
		data=monthRecordData;
		init();
	}

	public boolean isValid(){
		return valid;
	}
	
	public void setValid(boolean val){
		valid=val;
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
	
	

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
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

	@Override
	public LocalDate getDate() {
		int year = (monthYear>>4)+2016;
		int month = monthYear&0xF;
		if(month<1 || month>12)
		return null;
		LocalDate date = LocalDate.of(year, month, 1);
		if(date.isAfter(LocalDate.now()))
			return null;
		return date;
	}

	@Override
	public int getErrorChannel1() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getErrorChannel2() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "MonthRecordV1 [volume1=" + volume1 + ", energy1=" + energy1 + ", getDate()=" + getDate() + "]";
	}
}
