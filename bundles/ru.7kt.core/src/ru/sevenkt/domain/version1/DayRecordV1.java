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

@Length(value = 36)
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
				+ ", energy1=" + energy1 + "]";
	}
	@Override
	public void setPrevDayRecord(IDayRecord record) {
		prevDayRecord=(DayRecordV1) record;
		
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

}
