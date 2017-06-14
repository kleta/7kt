package ru.sevenkt.domain.version1;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.DayArchive;

@Length(value = 2232)
@RecordLength(value = 36)
public class DayArchiveV1 extends DayArchive{
	public static int MAX_MONTH_COUNT=6;
	

	public DayArchiveV1(byte[] dayData) throws Exception {
		super(dayData);
		parseData(DayRecordV1.class);
	}

	@Override
	public long getMaxMonthDeep() {
		return MAX_MONTH_COUNT;
	}
	
}
