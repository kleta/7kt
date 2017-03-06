package ru.sevenkt.domain.version1;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.IDayRecord;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.version3.DayRecordV3;
import ru.sevenkt.utils.DataUtils;

@Length(value = 56)
public class DayRecordV1 implements Cloneable, IDayRecord {

	private byte[] data;

	@Address(value = 0)
	@Length(value = 1)
	private int day;

	@Address(value = 1)
	@Length(value = 1)
	private int monthYear;

	@Address(value = 2)
	@Length(value = 2)
	@Parameter(Parameters.AVG_TEMP1)
	private int avgTemp1;

	@Address(value = 4)
	@Length(value = 2)
	@Parameter(Parameters.AVG_TEMP2)
	private int avgTemp2;

	@Address(value = 6)
	@Length(value = 2)
	@Parameter(Parameters.AVG_TEMP3)
	private int avgTemp3;

	@Address(value = 8)
	@Length(value = 2)
	@Parameter(Parameters.AVG_TEMP4)
	private int avgTemp4;

	@Address(value = 10)
	@Length(value = 1)
	@Parameter(Parameters.AVG_P1)
	private int avgPressure1;

	@Address(value = 11)
	@Length(value = 1)
	@Parameter(Parameters.AVG_P2)
	private int avgPressure2;

	@Address(value = 12)
	@Length(value = 4)
	@Parameter(Parameters.V1)
	private float volume1;

	@Address(value = 16)
	@Length(value = 4)
	@Parameter(Parameters.V2)
	private float volume2;

	@Address(value = 20)
	@Length(value = 4)
	@Parameter(Parameters.V3)
	private float volume3;

	@Address(value = 24)
	@Length(value = 4)
	@Parameter(Parameters.V4)
	private float volume4;

	@Address(value = 28)
	@Length(value = 4)
	@Parameter(Parameters.E1)
	private float energy1;

	@Address(value = 32)
	@Length(value = 4)
	@Parameter(Parameters.E2)
	private float energy2;

	@Address(value = 36)
	@Length(value = 4)
	@Parameter(Parameters.M1)
	private float weight1;

	@Address(value = 40)
	@Length(value = 4)
	@Parameter(Parameters.M2)
	private float weight2;

	@Address(value = 44)
	@Length(value = 4)
	@Parameter(Parameters.M3)
	private float weight3;

	@Address(value = 48)
	@Length(value = 4)
	@Parameter(Parameters.M4)
	private float weight4;

	@Address(value = 52)
	@Length(value = 1)
	@Parameter(Parameters.ERROR_BYTE1)
	private int errorChannel1;

	@Address(value = 53)
	@Length(value = 1)
	@Parameter(Parameters.ERROR_BYTE2)
	private int errorChannel2;

	@Address(value = 54)
	@Length(value = 1)
	@Parameter(Parameters.ERROR_TIME1)
	private int timeError1;

	@Address(value = 55)
	@Length(value = 1)
	@Parameter(Parameters.ERROR_TIME2)
	private int timeError2;

	private boolean valid;

	private DayRecordV1 prevDayRecord;

	public DayRecordV1(byte[] dayRecordData) throws Exception {
		data = dayRecordData;
		init();
	}

	public DayRecordV1() {
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

	public LocalDate getDate() {
		int year = (monthYear & 0xF0) / 16 + 2000;
		year = year < 2015 ? year + 16 : year;
		int month = monthYear & 0xF;
		if(month<1 || month>12 || day<1 || day>31)
			return null;
		LocalDate date = LocalDate.of(year, month, day);
		return date;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean val) {
		valid = val;
	}

	public DayRecordV1 minus(DayRecordV1 dr1) throws CloneNotSupportedException {
		DayRecordV1 dr2 = (DayRecordV1) this.clone();
		dr2.data = null;
		dr2.setAvgPressure1(getAvgPressure1() - dr1.getAvgPressure1());
		dr2.setAvgPressure2(getAvgPressure2() - dr1.getAvgPressure2());
		dr2.setAvgTemp1(getAvgTemp1() - dr1.getAvgTemp1());
		dr2.setAvgTemp2(getAvgTemp2() - dr1.getAvgTemp2());
		dr2.setAvgTemp3(getAvgTemp3() - dr1.getAvgTemp3()); 
		dr2.setAvgTemp4(getAvgTemp4() - dr1.getAvgTemp4());
		dr2.setEnergy1(new BigDecimal(getEnergy1()+"").subtract(new BigDecimal(dr1.getEnergy1()+"")).floatValue());
		dr2.setEnergy2(new BigDecimal(getEnergy2()+"").subtract(new BigDecimal(dr1.getEnergy2()+"")).floatValue());
		
		dr2.setVolume1(new BigDecimal(getVolume1()+"").subtract(new BigDecimal(dr1.getVolume1()+"")).floatValue());
		
		
		dr2.setVolume2(new BigDecimal(getVolume2()+"").subtract(new BigDecimal(dr1.getVolume2()+"")).floatValue());
		dr2.setVolume3(new BigDecimal(getVolume3()+"").subtract(new BigDecimal(dr1.getVolume3()+"")).floatValue());
		dr2.setVolume4(new BigDecimal(getVolume4()+"").subtract(new BigDecimal(dr1.getVolume4()+"")).floatValue());
		dr2.setWeight1(getWeight1() - dr1.getWeight1());
		dr2.setWeight2(getWeight2() - dr1.getWeight2());
		dr2.setWeight3(getWeight3() - dr1.getWeight3());
		dr2.setWeight4(getWeight4() - dr1.getWeight4());
		return dr2;
	}

	public boolean equalsValues(DayRecordV1 other) {
		if (Float.floatToIntBits(energy1) != Float.floatToIntBits(other.energy1))
			return false;
		if (Float.floatToIntBits(energy2) != Float.floatToIntBits(other.energy2))
			return false;
		if (Float.floatToIntBits(volume1) != Float.floatToIntBits(other.volume1))
			return false;
		if (Float.floatToIntBits(volume2) != Float.floatToIntBits(other.volume2))
			return false;
		if (Float.floatToIntBits(volume3) != Float.floatToIntBits(other.volume3))
			return false;
		if (Float.floatToIntBits(volume4) != Float.floatToIntBits(other.volume4))
			return false;
		return true;
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

	public Float getVolume1() {
		return volume1;
	}

	public void setVolume1(float volume1) {
		this.volume1 = volume1;
	}

	public Float getVolume2() {
		return volume2;
	}

	public void setVolume2(float volume2) {
		this.volume2 = volume2;
	}

	public Float getVolume3() {
		return volume3;
	}

	public void setVolume3(float volume3) {
		this.volume3 = volume3;
	}

	public Float getVolume4() {
		return volume4;
	}

	public void setVolume4(float volume4) {
		this.volume4 = volume4;
	}

	public Float getEnergy1() {
		return energy1;
	}

	public void setEnergy1(float energy1) {
		this.energy1 = energy1;
	}

	public Float getEnergy2() {
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

	@Override
	public IDayRecord minus(IDayRecord dr1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getErrorTime1() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getErrorTime2() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return "DayRecordV1 [avgTemp1=" + avgTemp1 + ", avgPressure1=" + avgPressure1 + ", volume1=" + volume1
				+ ", energy1=" + energy1 + ", weight1=" + weight1 + ", errorChannel1=" + errorChannel1
				+ ", errorChannel2=" + errorChannel2 + ", timeError1=" + timeError1 + ", timeError2=" + timeError2
				+ "]";
	}
	@Override
	public void setPrevDayRecord(IDayRecord record) {
		prevDayRecord=(DayRecordV1) record;
		
	}

}
