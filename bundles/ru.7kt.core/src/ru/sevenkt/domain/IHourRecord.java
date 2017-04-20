package ru.sevenkt.domain;

import java.time.LocalDateTime;

public interface IHourRecord {

	boolean isValid();

	LocalDateTime getDateTime();

	int getErrorChannel1();

	int getErrorChannel2();
	
	void setValid(boolean b);

}
