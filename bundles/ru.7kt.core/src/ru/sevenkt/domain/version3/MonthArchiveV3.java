package ru.sevenkt.domain.version3;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.domain.MonthArchive;

@Length(value = 2664)
@RecordLength(value=74)
public class MonthArchiveV3 extends MonthArchive{
	
	public static int MAX_YEAR_COUNT=3;
	
	
	
	
	public MonthArchiveV3(byte[] data) throws Exception {
		super(data);
		parseData(MonthRecordV3.class);
	}


	@Override
	public long getMaxYearsDeep() {
		return 3;
	}
}
