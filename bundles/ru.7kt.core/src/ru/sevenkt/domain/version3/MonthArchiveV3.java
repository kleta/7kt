package ru.sevenkt.domain.version3;

import java.time.LocalDate;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.IMonthRecord;

@Length(value = 2664)
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
