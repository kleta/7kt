package ru.sevenkt.domain.version3;

import java.time.LocalDate;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.HourArchive;

@Length(value = 41664)
@RecordLength(28)
public class HourArchiveV3 extends HourArchive{
	public static int MAX_DAY_COUNT = 59;

	

	public HourArchiveV3(byte[] data) throws Exception {
		super(data);
		parseData(HourRecordV3.class);
	}


	private int convertToMonthYearFormat(LocalDate recDateTime) {
		int year = recDateTime.getYear() - 2000;
		year = year == 15 ? year : year - 16;
		int monthYear = (year << 4) + recDateTime.getMonthValue();
		return monthYear;
	}



	@Override
	public long getMaxDaysDeep() {
		return MAX_DAY_COUNT;
	}


}
