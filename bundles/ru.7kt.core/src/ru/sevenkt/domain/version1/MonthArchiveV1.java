package ru.sevenkt.domain.version1;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.MonthArchive;

@Length(value = 2964)
public class MonthArchiveV1 extends MonthArchive{
	
	public static int MAX_YEAR_COUNT=3;
	
	
	
	
	public MonthArchiveV1(byte[] data) throws Exception {
		super(data);
		parseData(MonthRecordV1.class);
	}


	


	@Override
	public long getMaxYearsDeep() {
		return 3;
	}
}
