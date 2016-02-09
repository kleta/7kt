package ru.sevenkt.archive.domain;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ru.sevenkt.archive.utils.DataUtils;

@Length(80)
public class Settings {

	private byte[] data;

	@Address(value = 0)
	@Length(value = 1)
	private int archiveLength;

	@Address(value = 1)
	@Length(value = 1)
	private int archiveVersion;

	@Address(value = 2)
	@Length(value = 2)
	private int serialNumber;

	@Address(value = 4)
	@Length(value = 1)
	private int deviceVersion;

	@Address(value = 40)
	@Length(value = 2)
	private int wMin0;

	@Address(value = 42)
	@Length(value = 2)
	private int wMin1;

	@Address(value = 44)
	@Length(value = 2)
	private int wMax12;

	@Address(value = 46)
	@Length(value = 2)
	private int wMax34;

	@Address(value = 63)
	@Length(value = 1)
	private int netAddress;

	@Address(value = 64)
	@Length(value = 1)
	private int formulaNum;

	@Address(value = 65)
	@Length(value = 3)
	private float tempColdWaterSetting;

	@Address(value = 68)
	@Length(value = 3)
	private float volumeByImpulsSetting1;

	@Address(value = 71)
	@Length(value = 3)
	private float volumeByImpulsSetting2;

	@Address(value = 74)
	@Length(value = 3)
	private float volumeByImpulsSetting3;

	@Address(value = 77)
	@Length(value = 3)
	private float volumeByImpulsSetting4;

	public Settings(byte[] data) throws Exception {
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

	

}
