package ru.sevenkt.domain.version4;

import java.time.LocalDate;
import java.time.LocalDateTime;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.IDayRecord;

@Length(value = 13488)
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
