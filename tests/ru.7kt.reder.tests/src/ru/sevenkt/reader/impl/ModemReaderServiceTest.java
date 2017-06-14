package ru.sevenkt.reader.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.IJournalSettingsRecord;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.reader.services.IDeviceReaderService;
import ru.sevenkt.reader.services.impl.DeviceReaderImpl;

public class ModemReaderServiceTest {

	Connection con = new Connection("", "COM3", "89503047832", "");;
	private IDeviceReaderService readerService = new DeviceReaderImpl();

	@Test
	public void testReadFullArchive() throws Exception {		
		IArchive ar = readerService.readFullArchive(con);
		
	}
	
	@Test
	public void testReadHourArchive() throws Exception {		
		Set<ArchiveTypes> types=new HashSet<>();
		types.add(ArchiveTypes.HOUR);
		IArchive ar = readerService.readArchive(LocalDate.now().minusDays(15), LocalDate.now(), con, types, false);
		HourArchive ha = ar.getHourArchive();
		List<IHourRecord> hr = ha.getRecordsByDay(LocalDate.now().minusDays(1));
		for (IHourRecord iHourRecord : hr) {
			System.out.println(iHourRecord);
			assertNotNull(iHourRecord);
		}
		assertEquals(hr.size(), 24);
	}
	@Test
	public void testReadMonthArchive() throws Exception {		
		Set<ArchiveTypes> types=new HashSet<>();
		types.add(ArchiveTypes.MONTH);
		IArchive ar = readerService.readArchive(LocalDate.now().minusMonths(2), LocalDate.now(), con, types, false);
		MonthArchive ma = ar.getMonthArchive();
		
	}
	@Test
	public void testReadDayArchive() throws Exception {		
		Set<ArchiveTypes> types=new HashSet<>();
		types.add(ArchiveTypes.DAY);
		IArchive ar = readerService.readArchive(LocalDate.now().minusDays(110), LocalDate.now().minusDays(80), con, types, false);
		DayArchive da = ar.getDayArchive();		
	}
	@Test
	public void testJournal() throws Exception {		
		Set<ArchiveTypes> types=new HashSet<>();
		IArchive ar = readerService.readArchive(LocalDate.now().minusDays(110), LocalDate.now().minusDays(80), con, types, true);
		IJournalSettings j = ar.getJournalSettings();
		List<IJournalSettingsRecord> recs = j.getRecords();
		for (IJournalSettingsRecord iJournalSettingsRecord : recs) {
			System.out.println(iJournalSettingsRecord);			
		}
	}

}
