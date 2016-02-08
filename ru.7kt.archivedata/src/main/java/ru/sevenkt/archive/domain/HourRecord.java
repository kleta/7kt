package ru.sevenkt.archive.domain;

@Length(28)
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
	private int avgTemp1;
	
	@Address(value=4)
	@Length(value=2)
	private int avgTemp2;
	
	@Address(value=6)
	@Length(value=2)
	private int avgTemp3;
	
	@Address(value=8)
	@Length(value=2)
	private int avgTemp4;

	@Address(value=10)
	@Length(value=1)
	private int avgPressure1;
	
	@Address(value=11)
	@Length(value=1)
	private int avgPressure2;
	
	@Address(value=12)
	@Length(value=2)
	private float volume1;

	@Address(value=14)
	@Length(value=2)
	private float volume2;
	
	@Address(value=16)
	@Length(value=2)
	private float volume3;
	
	@Address(value=18)
	@Length(value=2)
	private float volume4;
	
	@Address(value=20)
	@Length(value=3)
	private float energy1;

	@Address(value=23)
	@Length(value=3)
	private float energy2;
	
	
	@Address(value=26)
	@Length(value=1)
	private int errorChannel1;
	
	@Address(value=27)
	@Length(value=1)
	private int errorChannel2;

	public HourRecord(byte[] hourRecordData) {
		data=hourRecordData;
	}

	
}
