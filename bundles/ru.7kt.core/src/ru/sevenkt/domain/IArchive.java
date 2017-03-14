package ru.sevenkt.domain;

import java.io.File;

public interface IArchive {

	ISettings getSettings();

	MonthArchive getMonthArchive();

	ICurrentData getCurrentData();

	HourArchive getHourArchive();

	DayArchive getDayArchive();

	IJournalSettings getJournalSettings();
	
	File toFile();
	
}
