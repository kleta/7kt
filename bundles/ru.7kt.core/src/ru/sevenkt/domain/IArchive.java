package ru.sevenkt.domain;

public interface IArchive {

	ISettings getSettings();

	MonthArchive getMonthArchive();

	ICurrentData getCurrentData();

	HourArchive getHourArchive();

	DayArchive getDayArchive();

	IJournalSettings getJournalSettings();
	
}
