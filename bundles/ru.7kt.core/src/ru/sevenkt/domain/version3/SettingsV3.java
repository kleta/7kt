package ru.sevenkt.domain.version3;

import java.lang.reflect.Field;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.utils.DataUtils;

@Length(80)
public class SettingsV3 implements ISettings{
	public static Integer[] SUPPORTED_ARCHIVE_VERSION = { 3 };

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

	@Address(value = 32)
	@Length(value = 2)
	private int wMin0;

	@Address(value = 34)
	@Length(value = 2)
	private int wMin1;

	@Address(value = 36)
	@Length(value = 2)
	private int wMax12;

	@Address(value = 38)
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

	public SettingsV3(byte[] data) throws Exception {
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
					float value = DataUtils.getFloat24Value(bytes);
					field.set(this, value);
				}
			}
		}
	}

	public boolean isVersionSupport() {
		return Arrays.asList(SUPPORTED_ARCHIVE_VERSION).contains(archiveVersion);
	}

	public static Integer[] getSUPPORTED_ARCHIVE_VERSION() {
		return SUPPORTED_ARCHIVE_VERSION;
	}

	public static void setSUPPORTED_ARCHIVE_VERSION(Integer[] sUPPORTED_ARCHIVE_VERSION) {
		SUPPORTED_ARCHIVE_VERSION = sUPPORTED_ARCHIVE_VERSION;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getArchiveLength() {
		return archiveLength;
	}

	public void setArchiveLength(int archiveLength) {
		this.archiveLength = archiveLength;
	}

	public int getArchiveVersion() {
		return archiveVersion;
	}

	public void setArchiveVersion(int archiveVersion) {
		this.archiveVersion = archiveVersion;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(int deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public int getwMin0() {
		return wMin0;
	}

	public void setwMin0(int wMin0) {
		this.wMin0 = wMin0;
	}

	public int getwMin1() {
		return wMin1;
	}

	public void setwMin1(int wMin1) {
		this.wMin1 = wMin1;
	}

	public int getwMax12() {
		return wMax12;
	}

	public void setwMax12(int wMax12) {
		this.wMax12 = wMax12;
	}

	public int getwMax34() {
		return wMax34;
	}

	public void setwMax34(int wMax34) {
		this.wMax34 = wMax34;
	}

	public int getNetAddress() {
		return netAddress;
	}

	public void setNetAddress(int netAddress) {
		this.netAddress = netAddress;
	}

	public int getFormulaNum() {
		return formulaNum;
	}

	public void setFormulaNum(int formulaNum) {
		this.formulaNum = formulaNum;
	}

	public float getTempColdWaterSetting() {
		return tempColdWaterSetting;
	}

	public void setTempColdWaterSetting(float tempColdWaterSetting) {
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

}
