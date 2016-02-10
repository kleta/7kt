package ru.sevenkt.archive.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Length(value = 41664)
public class HourArchive {
	public static int MAX_DAY_COUNT = 60;

	private byte[] data;

	public HourArchive(byte[] data) {
		this.data = data;
	}

	public HourRecord getHourRecord(LocalDateTime recDateTime, LocalDateTime archiveCurrenDatetime) throws Exception {
		int hour = recDateTime.getHour();
		int month = recDateTime.getMonth().getValue();
		int day = recDateTime.getDayOfMonth();
		if (recDateTime.isBefore(archiveCurrenDatetime.minusDays(MAX_DAY_COUNT)))
			throw new Exception("√лубина архива " + MAX_DAY_COUNT + " суток. «апрашиваема€ дата " + recDateTime
					+ " выходит за пределы глубины хранени€");

		Length annotationLength = HourRecord.class.getAnnotation(Length.class);
		int size = annotationLength.value();

		int address = (day - 1) * 24 * size + hour * size;
		if (month % 2 == 0)
			address += 31 * 24 * size;
		byte[] hourRecordData = new byte[size];
		for (int i = 0; i < size; i++) {
			hourRecordData[i] = data[address + i];
		}
		HourRecord hourRecord = new HourRecord(hourRecordData, hour);
		if (!hourRecord.getDateTime().equals(recDateTime)) {
			hourRecord.setAvgPressure1(0);
			hourRecord.setAvgPressure2(0);
			hourRecord.setAvgTemp1(0);
			hourRecord.setAvgTemp2(0);
			hourRecord.setAvgTemp3(0);
			hourRecord.setAvgTemp4(0);
			hourRecord.setDay(recDateTime.getDayOfMonth());
			hourRecord.setEnergy1(0);
			hourRecord.setEnergy2(0);
			hourRecord.setMonthYear(convertToMonthYearFormat(recDateTime.toLocalDate()));
			hourRecord.setHour(recDateTime.getHour());
			hourRecord.setVolume1(0);
			hourRecord.setVolume2(0);
			hourRecord.setVolume3(0);
			hourRecord.setVolume4(0);
		}
		return hourRecord;
	}

	private int convertToMonthYearFormat(LocalDate recDateTime) {
		int year = recDateTime.getYear() - 2000;
		year = year == 15 ? year : year - 16;
		int monthYear = (year << 4) + recDateTime.getMonthValue();
		return monthYear;
	}

	public DayRecord getDayConsumption(LocalDate requestDate, LocalDateTime archiveCurrenDatetime) throws Exception {
		LocalDateTime startDateTime = LocalDateTime.of(requestDate, LocalTime.of(0, 0)).plusHours(1);
		DayRecord dayRecord = new DayRecord();
		dayRecord.setDay(requestDate.getDayOfMonth());
		dayRecord.setMonthYear(convertToMonthYearFormat(requestDate));

		while (startDateTime.isBefore(LocalDateTime.of(requestDate.plusDays(1), LocalTime.of(0, 1)))) {
			HourRecord hr = getHourRecord(startDateTime, archiveCurrenDatetime);
			String s = String.format("Date %s p1=%d p2=%d t1=%d t2=%d t3=%d t4=%d", hr.getDateTime(), hr.getAvgPressure1(),
					hr.getAvgPressure2(), hr.getAvgTemp1(), hr.getAvgTemp2(), hr.getAvgTemp3(), hr.getAvgTemp4());
			System.out.println(s);
			dayRecord.setAvgPressure1(dayRecord.getAvgPressure1() + hr.getAvgPressure1());
			dayRecord.setAvgPressure2(dayRecord.getAvgPressure2() + hr.getAvgPressure2());
			dayRecord.setAvgTemp1(dayRecord.getAvgTemp1() + hr.getAvgTemp1());
			dayRecord.setAvgTemp2(dayRecord.getAvgTemp2() + hr.getAvgTemp2());
			dayRecord.setAvgTemp3(dayRecord.getAvgTemp3() + hr.getAvgTemp3());
			dayRecord.setAvgTemp4(dayRecord.getAvgTemp4() + hr.getAvgTemp4());

			dayRecord.setEnergy1(dayRecord.getEnergy1() + hr.getEnergy1());
			dayRecord.setEnergy2(dayRecord.getEnergy2() + hr.getEnergy2());

			dayRecord.setVolume1(dayRecord.getVolume1() + hr.getVolume1());
			dayRecord.setVolume2(dayRecord.getVolume2() + hr.getVolume2());
			dayRecord.setVolume3(dayRecord.getVolume3() + hr.getVolume3());
			dayRecord.setVolume4(dayRecord.getVolume4() + hr.getVolume4());
			startDateTime = startDateTime.plusHours(1);

		}
		dayRecord.setAvgPressure1(dayRecord.getAvgPressure1() / 24);
		dayRecord.setAvgPressure2(dayRecord.getAvgPressure2() / 24);
		dayRecord.setAvgTemp1(dayRecord.getAvgTemp1() / 24);
		dayRecord.setAvgTemp2(dayRecord.getAvgTemp2() / 24);
		dayRecord.setAvgTemp3(dayRecord.getAvgTemp3() / 24);
		dayRecord.setAvgTemp4(dayRecord.getAvgTemp4() / 24);
		return dayRecord;
	}
}
