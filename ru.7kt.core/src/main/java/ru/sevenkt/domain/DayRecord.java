package ru.sevenkt.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import lombok.Data;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.utils.DataUtils;

@Length(value = 56)
@Data
public class DayRecord implements Cloneable{

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
	private int errorChannel1;

	@Address(value = 53)
	@Length(value = 1)
	private int errorChannel2;

	@Address(value = 54)
	@Length(value = 1)
	private int timeError1;

	@Address(value = 55)
	@Length(value = 1)
	private int timeError2;

	private boolean valid;

	public DayRecord(byte[] dayRecordData) throws Exception {
		data = dayRecordData;
		init();
	}
	public DayRecord() {		
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
		int year = (monthYear & 0xF0)/16 + 2000;	
		year=year<2015 ?year+16:year;	
		int month = monthYear & 0xF;
		LocalDate date = LocalDate.of(year, month, day);
		return date;
	}
	public boolean isValid() {	
		return valid;
	}
	
	public void setValid(boolean val){
		valid=val;
	}
	public DayRecord minus(DayRecord dr1) throws CloneNotSupportedException {
		DayRecord dr2 = (DayRecord) this.clone();
		dr2.data=null;
		dr2.setAvgPressure1(getAvgPressure1()-dr1.getAvgPressure1());
		dr2.setAvgPressure2(getAvgPressure2()-dr1.getAvgPressure2());
		dr2.setAvgTemp1(getAvgTemp1()-dr1.getAvgTemp1());
		dr2.setAvgTemp2(getAvgTemp2()-dr1.getAvgTemp2());
		dr2.setAvgTemp3(getAvgTemp3()-dr1.getAvgTemp3());
		dr2.setAvgTemp4(getAvgTemp4()-dr1.getAvgTemp4());
		dr2.setEnergy1(getEnergy1()-dr1.getEnergy1());
		dr2.setEnergy2(getEnergy2()-dr1.getEnergy2());
		dr2.setVolume1(getVolume1()-dr1.getVolume1());
		dr2.setVolume2(getVolume2()-dr1.getVolume2());
		dr2.setVolume3(getVolume3()-dr1.getVolume3());
		dr2.setVolume4(getVolume4()-dr1.getVolume4());
		dr2.setWeight1(getWeight1()-dr1.getWeight1());
		dr2.setWeight2(getWeight2()-dr1.getWeight2());
		dr2.setWeight3(getWeight3()-dr1.getWeight3());
		dr2.setWeight4(getWeight4()-dr1.getWeight4());
		return dr2;
	}
	
	public boolean equalsValues(DayRecord other) {
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
	
	
}
