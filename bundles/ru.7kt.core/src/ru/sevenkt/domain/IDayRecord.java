package ru.sevenkt.domain;

import java.time.LocalDate;
import java.util.List;

public interface IDayRecord {

	boolean isValid();

	IDayRecord minus(IDayRecord dr1);

	LocalDate getDate();

	Float getVolume1();

	Float getVolume2();

	Float getVolume3();

	Float getVolume4();

	Float getEnergy1();

	Float getEnergy2();

	int getErrorChannel1();

	int getErrorChannel2();
	
	int getErrorTime1();

	int getErrorTime2();
	
	void setPrevDayRecord(IDayRecord record);

}
