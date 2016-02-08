package ru.sevenkt.archive.domain;

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
	private char year;
	
	@Address(value=17)
	@Length(value=1)
	private char month;

	@Address(value=18)
	@Length(value=1)
	private char day;

	@Address(value=19)
	@Length(value=1)
	private char hour;

	@Address(value=20)
	@Length(value=4)
	private long workHour;
	
	@Address(value=24)
	@Length(value=4)
	private float volume1;

	@Address(value=28)
	@Length(value=4)
	private float volume2;
	
	@Address(value=32)
	@Length(value=4)
	private float volume3;
	
	@Address(value=36)
	@Length(value=4)
	private float volume4;
	
	@Address(value=40)
	@Length(value=4)
	private float energy1;

	@Address(value=44)
	@Length(value=4)
	private float energy2;
	
	@Address(value=48)
	@Length(value=4)
	private float weight1;

	@Address(value=52)
	@Length(value=4)
	private float weight2;
	
	@Address(value=56)
	@Length(value=4)
	private float weight3;
	
	@Address(value=60)
	@Length(value=4)
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
	private int temeError2;
	
	
	
	public MonthRecord(byte[] monthRecordData) {
		data=monthRecordData;
	}
}
