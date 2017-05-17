package ru.sevenkt.domain.version1;

import java.time.LocalDate;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.HourArchive;

@Length(value = 29328)
public class HourArchiveV1 extends HourArchive{
	public static int MAX_DAY_COUNT = 59;

	

	public HourArchiveV1(byte[] data) throws Exception {
		super(data);
		parseData(HourRecordV1.class);
	}


	@Override
	public long getMaxDaysDeep() {
		return MAX_DAY_COUNT;
	}
}
