package ru.sevenkt.archive.domain;

@Length(value=56)
public class DayRecord {
	
	
	private byte[] data;

	public DayRecord(byte[] dayRecordData) {
		data=dayRecordData;
	}

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
	@Length(value=4)
	private float volume1;

	@Address(value=16)
	@Length(value=4)
	private float volume2;
	
	@Address(value=20)
	@Length(value=4)
	private float volume3;
	
	@Address(value=24)
	@Length(value=4)
	private float volume4;
	
	@Address(value=28)
	@Length(value=4)
	private float energy1;

	@Address(value=32)
	@Length(value=4)
	private float energy2;
	
	@Address(value=36)
	@Length(value=4)
	private float weight1;

	@Address(value=40)
	@Length(value=4)
	private float weight2;
	
	@Address(value=44)
	@Length(value=4)
	private float weight3;
	
	@Address(value=48)
	@Length(value=4)
	private float weight4;
	
	@Address(value=52)
	@Length(value=1)
	private int errorChannel1;
	
	@Address(value=53)
	@Length(value=1)
	private int errorChannel2;
	
	@Address(value=54)
	@Length(value=1)
	private int timeError1;
	
	@Address(value=55)
	@Length(value=1)
	private int temeError2;
	
}
