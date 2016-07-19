package ru.sevenkt.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ru.sevenkt.annotations.Length;

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
		if (recDateTime.equals(dateTime)) {
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

	public DayRecord getDayConsumption(LocalDate requestDate, LocalDateTime archiveCurrenDatetime, Settings settings)
			throws Exception {
		LocalDateTime startDateTime = LocalDateTime.of(requestDate, LocalTime.of(0, 0)).plusHours(1);

		DayRecord dayRecord = new DayRecord();
		dayRecord.setDay(requestDate.getDayOfMonth());
		dayRecord.setMonthYear(convertToMonthYearFormat(requestDate));

		while (startDateTime.isBefore(LocalDateTime.of(requestDate.plusDays(1), LocalTime.of(0, 1)))) {
			HourRecord hr = getHourRecord(startDateTime, archiveCurrenDatetime);
			if (hr.isValid()) {
				dayRecord.setAvgPressure1(dayRecord.getAvgPressure1() + hr.getAvgPressure1());
				dayRecord.setAvgPressure2(dayRecord.getAvgPressure2() + hr.getAvgPressure2());
				dayRecord.setAvgTemp1(dayRecord.getAvgTemp1() + hr.getAvgTemp1());
				dayRecord.setAvgTemp2(dayRecord.getAvgTemp2() + hr.getAvgTemp2());
				dayRecord.setAvgTemp3(dayRecord.getAvgTemp3() + hr.getAvgTemp3());
				dayRecord.setAvgTemp4(dayRecord.getAvgTemp4() + hr.getAvgTemp4());

				dayRecord.setEnergy1(new BigDecimal(dayRecord.getEnergy1() + "")
						.add(new BigDecimal(hr.getEnergy1()+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());
				dayRecord.setEnergy2(new BigDecimal(dayRecord.getEnergy2() + "")
						.add(new BigDecimal(hr.getEnergy2()+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());

				float hrV1 = hr.getVolume1() * settings.getVolumeByImpulsSetting1();
				dayRecord.setVolume1(new BigDecimal(dayRecord.getVolume1() + "")
						.add(new BigDecimal(hrV1+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());

				float hrV2 = hr.getVolume2() * settings.getVolumeByImpulsSetting2();
				dayRecord.setVolume2(new BigDecimal(dayRecord.getVolume2() + "")
						.add(new BigDecimal(hrV2+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());

				float hrV3 = hr.getVolume3() * settings.getVolumeByImpulsSetting3();
				dayRecord.setVolume3(new BigDecimal(dayRecord.getVolume3() + "")
						.add(new BigDecimal(hrV3+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());

				float hrV4 = hr.getVolume4() * settings.getVolumeByImpulsSetting4();
				dayRecord.setVolume4(new BigDecimal(dayRecord.getVolume4() + "")
						.add(new BigDecimal(hrV4+"").setScale(32, BigDecimal.ROUND_HALF_UP)).floatValue());
			}
//			System.out.println(startDateTime);
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

	public static int getMAX_DAY_COUNT() {
		return MAX_DAY_COUNT;
	}

	public static void setMAX_DAY_COUNT(int mAX_DAY_COUNT) {
		MAX_DAY_COUNT = mAX_DAY_COUNT;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
