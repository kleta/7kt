package ru.sevenkt.domain.version4;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.DayArchive;

@Length(value = 10788)
@RecordLength(value = 58)
public class DayArchiveV4 extends DayArchive{
	public static int MAX_MONTH_COUNT=6;
	
	

	public DayArchiveV4(byte[] dayData) throws Exception {
		super(dayData);
		parseData(DayRecordV4.class);
	}


	@Override
	public long getMaxMonthDeep() {
		return MAX_MONTH_COUNT;
	}
	
}
