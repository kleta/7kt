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

@Length(28)
@Data
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
	@Parameter(Parameters.W1)
	private int volume1;

	@Address(value=14)
	@Length(value=2)
	@Parameter(Parameters.W2)
	private int volume2;
	
	@Address(value=16)
	@Length(value=2)
	@Parameter(Parameters.W3)
	private int volume3;
	
	@Address(value=18)
	@Length(value=2)
	@Parameter(Parameters.W4)
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
	private int errorChannel1;
	
	@Address(value=27)
	@Length(value=1)
	private int errorChannel2;

	private int hour;

	private boolean valid;

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
		int year = (monthYear & 0xF0)/16 + 2000;	
		year=year<2015 ?year+16:year;	
		int month = monthYear & 0xF;
		LocalDateTime date = LocalDateTime.of(year, month, day, hour, 0);
		return date;
	}
	public void setValid(boolean b) {
		valid=b;		
	}
	 public boolean isValid(){
		 return valid;
	 }
	
}
