package ru.sevenkt.domain;

import java.time.LocalDateTime;

public interface IJournalSettingsRecord {

	LocalDateTime getDateTime();

	long getWorkHour();

	JournalEvents getEvent();

}
