package ru.sevenkt.archive.domain;

@Length(value = 48)
public class CurrentData {
	
	@Address(value=0)
	@Length(value=1)
	private char year;
	
	@Address(value=1)
	@Length(value=1)
	private char month;

	@Address(value=2)
	@Length(value=1)
	private char day;

	@Address(value=3)
	@Length(value=1)
	private char hour;//83

	@Address(value=4)
	@Length(value=4)
	private long workHour;//84
	
	@Address(value=8)
	@Length(value=4)
	private float volume1;

	@Address(value=12)
	@Length(value=4)
	private float volume2;
	
	@Address(value=16)
	@Length(value=4)
	private float volume3;
	
	@Address(value=20)
	@Length(value=4)
	private float volume4;
	
	@Address(value=24)
	@Length(value=4)
	private float energy1;

	@Address(value=28)
	@Length(value=4)
	private float energy2;
	
	@Address(value=32)
	@Length(value=4)
	private float weight1;

	@Address(value=36)
	@Length(value=4)
	private float weight2;
	
	@Address(value=40)
	@Length(value=4)
	private float weight3;
	
	@Address(value=44)
	@Length(value=4)
	private float weight4;
	
	private byte[] data;

	public CurrentData(byte[] data) {
		super();
		this.data = data;
	}
	
	
}
