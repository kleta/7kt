package ru.sevenkt.domain.version4;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.MonthArchive;

@Length(value = 2964)
public class MonthArchiveV4 extends MonthArchive{
	
	
	
	
	public MonthArchiveV4(byte[] data) throws Exception {
		super(data);
		parseData(MonthRecordV4.class);
	}

	@Override
	public long getMaxYearsDeep() {
		return 3;
	}
}
