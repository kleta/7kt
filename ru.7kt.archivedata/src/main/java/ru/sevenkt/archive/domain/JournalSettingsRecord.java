package ru.sevenkt.archive.domain;

@Length(10)
public class JournalSettingsRecord {
	
	@Address(value=0)
	@Length(value=1)
	private int hour;
	
	@Address(value=1)
	@Length(value=1)
	private int day;
	
	@Address(value=2)
	@Length(value=1)
	private int monthYear;

	@Address(value=3)
	@Length(value=4)
	private long workHour;
	
	@Address(value=7)
	@Length(value=1)
	private JournalEvents event;
}
