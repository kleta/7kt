package ru.sevenkt.archive.domain;

public class Archive {
	public static int ADDR_SETTINGS=0;
	
	public static int ADDR_CURRENT_DATA=0x40;

	public static int ADDR_MONTH_DATA=0x90;

	public static int ADDR_DAY_DATA=0x200;

	public static int ADDR_HOUR_DATA=0xB00;
	
	private Settings settings;
	
	private CurrentData currentData;
	
	private MonthArchive monthArchive;
	
	private DayArchive dayArchive;
	
	private HourArchive hourArchive;
}
