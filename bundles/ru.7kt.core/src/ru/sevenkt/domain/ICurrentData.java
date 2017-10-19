package ru.sevenkt.domain;

import java.time.LocalDateTime;

public interface ICurrentData {

	LocalDateTime getCurrentDateTime();

	long getWorkHour();

	float getVolume1();

	float getVolume2();

	float getVolume3();

	float getVolume4();

	float getEnergy1();

	float getEnergy2();

	float getWeight1();

	float getWeight2();

	float getWeight3();

	float getWeight4();

}
