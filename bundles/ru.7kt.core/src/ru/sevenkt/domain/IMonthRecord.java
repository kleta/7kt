package ru.sevenkt.domain;

import java.time.LocalDate;

public interface IMonthRecord {

	boolean isValid();

	LocalDate getDate();

	int getErrorChannel1();

	int getErrorChannel2();
	
	void setValid(boolean valid);

}
