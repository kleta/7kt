package ru.sevenkt.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ru.sevenkt.annotation.Length;

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
		LocalDateTime minusDays = archiveCurrenDatetime.minusDays(MAX_DAY_COUNT + 1);
		if (recDateTime.isBefore(minusDays))
			throw new Exception("Глубина часового архива " + MAX_DAY_COUNT + " суток. Данные запрашиваемые за дату "
					+ recDateTime + " превышают глубину хранения");

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
		LocalDateTime dateTime = hourRecord.getDateTime();
		if (dateTime.equals(recDateTime)) {
			hourRecord.setValid(true);
		} else
			hourRecord.setValid(false);
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
			if (hr.isValid()) {
				String s = String.format("Date %s v1=%d v2=%d v3=%d v4=%d e1=%f e2=%f", hr.getDateTime(),
						hr.getVolume1(), hr.getVolume2(), hr.getVolume3(), hr.getVolume4(),
						hr.getEnergy1(), hr.getEnergy2());
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

			}
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
