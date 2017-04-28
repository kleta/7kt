package ru.sevenkt.domain.version3;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.utils.DataUtils;

@Length(28)
public class HourRecordV3 implements IHourRecord{
	
	private byte[] data;
	
	@Address(value=0)
	@Length(value=1)
	private int day;
	
	@Address(value=1)
	@Length(value=1)
	private int monthYear;
	
	@Address(value=2)
	@Length(value=2)
	@Parameter(Parameters.AVG_TEMP1)
	private int avgTemp1;
	
	@Address(value=4)
	@Length(value=2)
	@Parameter(Parameters.AVG_TEMP2)
	private int avgTemp2;
	
	@Address(value=6)
	@Length(value=2)
	@Parameter(Parameters.AVG_TEMP3)
	private int avgTemp3;
	
	@Address(value=8)
	@Length(value=2)
	@Parameter(Parameters.AVG_TEMP4)
	private int avgTemp4;

	@Address(value=10)
	@Length(value=1)
	@Parameter(Parameters.AVG_P1)
	private int avgPressure1;
	
	@Address(value=11)
	@Length(value=1)
	@Parameter(Parameters.AVG_P2)
	private int avgPressure2;
	
	@Address(value=12)
	@Length(value=2)
	@Parameter(Parameters.V1)
	private int volume1;

	@Address(value=14)
	@Length(value=2)
	@Parameter(Parameters.V2)
	private int volume2;
	
	@Address(value=16)
	@Length(value=2)
	@Parameter(Parameters.V3)
	private int volume3;
	
	@Address(value=18)
	@Length(value=2)
	@Parameter(Parameters.V4)
	private int volume4;
	
	@Address(value=20)
	@Length(value=3)
	@Parameter(Parameters.E1)
	private float energy1;

	@Address(value=23)
	@Length(value=3)
	@Parameter(Parameters.E2)
	private float energy2;
	
	
	@Address(value=26)
	@Length(value=1)
	@Parameter(Parameters.ERROR_BYTE1)
	private int errorChannel1;
	
	@Address(value=27)
	@Length(value=1)
	@Parameter(Parameters.ERROR_BYTE2)
	private int errorChannel2;
	
	@Parameter(Parameters.ERROR_TIME1)
	private int timeError1;
	
	@Parameter(Parameters.ERROR_TIME2)
	private int timeError2;

	private int hour;

	private boolean valid;

	public HourRecordV3(byte[] hourRecordData, int hour) throws Exception {
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
		int year = (monthYear & 0xF0)/16 + 2000;	
		year=year<2015 ?year+16:year;	
		int month = monthYear & 0xF;
		if(month<1 || month>12 || day<1 || day>31 || hour<0 || hour>24)
			return null;
		LocalDateTime date = LocalDateTime.of(year, month, day, hour, 0);
		return date;
	}
	public void setValid(boolean b) {
		valid=b;		
	}
	 public boolean isValid(){
		 return valid;
	 }
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(int monthYear) {
		this.monthYear = monthYear;
	}
	public int getAvgTemp1() {
		return avgTemp1;
	}
	public void setAvgTemp1(int avgTemp1) {
		this.avgTemp1 = avgTemp1;
	}
	public int getAvgTemp2() {
		return avgTemp2;
	}
	public void setAvgTemp2(int avgTemp2) {
		this.avgTemp2 = avgTemp2;
	}
	public int getAvgTemp3() {
		return avgTemp3;
	}
	public void setAvgTemp3(int avgTemp3) {
		this.avgTemp3 = avgTemp3;
	}
	public int getAvgTemp4() {
		return avgTemp4;
	}
	public void setAvgTemp4(int avgTemp4) {
		this.avgTemp4 = avgTemp4;
	}
	public int getAvgPressure1() {
		return avgPressure1;
	}
	public void setAvgPressure1(int avgPressure1) {
		this.avgPressure1 = avgPressure1;
	}
	public int getAvgPressure2() {
		return avgPressure2;
	}
	public void setAvgPressure2(int avgPressure2) {
		this.avgPressure2 = avgPressure2;
	}
	public int getVolume1() {
		return volume1;
	}
	public void setVolume1(int volume1) {
		this.volume1 = volume1;
	}
	public int getVolume2() {
		return volume2;
	}
	public void setVolume2(int volume2) {
		this.volume2 = volume2;
	}
	public int getVolume3() {
		return volume3;
	}
	public void setVolume3(int volume3) {
		this.volume3 = volume3;
	}
	public int getVolume4() {
		return volume4;
	}
	public void setVolume4(int volume4) {
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
	public int getErrorChannel1() {
		return errorChannel1;
	}
	public void setErrorChannel1(int errorChannel1) {
		this.errorChannel1 = errorChannel1;
	}
	public int getErrorChannel2() {
		return errorChannel2;
	}
	public void setErrorChannel2(int errorChannel2) {
		this.errorChannel2 = errorChannel2;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	@Override
	public String toString() {
		return "HourRecordV3 [avgTemp1=" + avgTemp1 + ", avgPressure1=" + avgPressure1 + ", volume1=" + volume1
				+ ", energy1=" + energy1 + ", errorChannel1=" + errorChannel1 + ", errorChannel2=" + errorChannel2
				+ ", hour=" + hour + "]";
	}
	
	
}
