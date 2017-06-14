package ru.sevenkt.domain.version4;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.HourArchive;

@Length(value = 44640)
@RecordLength(30)
public class HourArchiveV4 extends HourArchive {
	public static int MAX_DAY_COUNT = 59;

	public HourArchiveV4(byte[] data) throws Exception {
		super(data);
		parseData(HourRecordV4.class);
	}

	@Override
	public long getMaxDaysDeep() {
		return MAX_DAY_COUNT;
	}
}
