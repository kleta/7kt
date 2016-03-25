package ru.sevenkt.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.utils.DataUtils;

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
	@Parameter(Parameters.WORK)
	private int workHour;
	
	@Address(value=24)
	@Length(value=4)
	@Parameter(Parameters.V1)
	private float volume1;

	@Address(value=28)
	@Length(value=4)
	@Parameter(Parameters.V2)
	private float volume2;
	
	@Address(value=32)
	@Length(value=4)
	@Parameter(Parameters.V3)
	private float volume3;
	
	@Address(value=36)
	@Length(value=4)
	@Parameter(Parameters.V4)
	private float volume4;
	
	@Address(value=40)
	@Length(value=4)
	@Parameter(Parameters.E1)
	private float energy1;

	@Address(value=44)
	@Length(value=4)
	@Parameter(Parameters.E2)
	private float energy2;
	
	@Address(value=48)
	@Length(value=4)
	@Parameter(Parameters.M1)
	private float weight1;

	@Address(value=52)
	@Length(value=4)
	@Parameter(Parameters.M2)
	private float weight2;
	
	@Address(value=56)
	@Length(value=4)
	@Parameter(Parameters.M3)
	private float weight3;
	
	@Address(value=60)
	@Length(value=4)
	@Parameter(Parameters.M4)
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
	
	private boolean valid;
	
	public MonthRecord(byte[] monthRecordData) throws Exception {
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
	
	public LocalDate getDate(){
		if(month<1 || month>12 || day<1 || day>31)
			return null;
		LocalDate date = LocalDate.of(year+2000, month, day);
		return date;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getFormulaNum() {
		return formulaNum;
	}

	public void setFormulaNum(int formulaNum) {
		this.formulaNum = formulaNum;
	}

	public int getTempColdWaterSetting() {
		return tempColdWaterSetting;
	}

	public void setTempColdWaterSetting(int tempColdWaterSetting) {
		this.tempColdWaterSetting = tempColdWaterSetting;
	}

	public float getVolumeByImpulsSetting1() {
		return volumeByImpulsSetting1;
	}

	public void setVolumeByImpulsSetting1(float volumeByImpulsSetting1) {
		this.volumeByImpulsSetting1 = volumeByImpulsSetting1;
	}

	public float getVolumeByImpulsSetting2() {
		return volumeByImpulsSetting2;
	}

	public void setVolumeByImpulsSetting2(float volumeByImpulsSetting2) {
		this.volumeByImpulsSetting2 = volumeByImpulsSetting2;
	}

	public float getVolumeByImpulsSetting3() {
		return volumeByImpulsSetting3;
	}

	public void setVolumeByImpulsSetting3(float volumeByImpulsSetting3) {
		this.volumeByImpulsSetting3 = volumeByImpulsSetting3;
	}

	public float getVolumeByImpulsSetting4() {
		return volumeByImpulsSetting4;
	}

	public void setVolumeByImpulsSetting4(float volumeByImpulsSetting4) {
		this.volumeByImpulsSetting4 = volumeByImpulsSetting4;
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

	public int getWorkHour() {
		return workHour;
	}

	public void setWorkHour(int workHour) {
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

	public int getTimeError1() {
		return timeError1;
	}

	public void setTimeError1(int timeError1) {
		this.timeError1 = timeError1;
	}

	public int getTimeError2() {
		return timeError2;
	}

	public void setTimeError2(int timeError2) {
		this.timeError2 = timeError2;
	}
}
