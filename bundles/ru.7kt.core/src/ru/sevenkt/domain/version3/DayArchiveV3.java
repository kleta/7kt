package ru.sevenkt.domain.version3;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.DayArchive;

@Length(value = 10416)
@RecordLength(value = 56)
public class DayArchiveV3 extends DayArchive{
	public static int MAX_MONTH_COUNT=6;
	

	public DayArchiveV3(byte[] dayData) throws Exception {
		super(dayData);
		parseData(DayRecordV3.class);
	}

	@Override
	public long getMaxMonthDeep() {
		return MAX_MONTH_COUNT;
	}
	
}
