package ru.sevenkt.unittests;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

import ru.sevenkt.archive.services.impl.ArchiveServiceImpl;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.version3.ArchiveV3;

public class ArchiveServiceTest {
	ArchiveServiceImpl as = new ArchiveServiceImpl();

	@Test
	public void testArchiveV3() throws Exception {
		System.out.println("Архив V3-----------------------------------------------------------------");
		File folder = new File("resources/V3");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName()+"==============================================");
				parseArchive(listOfFiles[i]);
				System.out.println();
			}
		}
	}
	
	@Test
	public void testArchiveV4() throws Exception {
		System.out.println("Архив V4-----------------------------------------------------------------");
		File folder = new File("resources/V4");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName()+"==============================================");
				parseArchive(listOfFiles[i]);
				System.out.println();
			}
		}
	}
	@Test
	public void testArchiveV1() throws Exception {
		System.out.println("Архив V1-----------------------------------------------------------------");
		File folder = new File("resources/V1");
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName()+"==============================================");
				parseArchive(listOfFiles[i]);
				System.out.println();
			}
		}
	}
	
	public void testSpreadHourData() throws Exception{
		File file = new File("resources/V3/02016_2016-02-04_13-00.bin");
		IArchive archive = as.readArchiveFromFile(file);
	}
	
	@Test
	public void testConvertDateToAddress() throws Exception{
		int adr = ArchiveFactory.convertDateToAddress(LocalDate.parse("2015-02-01"), ArchiveV3.class, ArchiveTypes.MONTH);
		assertEquals(adr, 374);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2016-02-01"), ArchiveV3.class, ArchiveTypes.MONTH);
		assertEquals(adr, 1262);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2017-12-01"), ArchiveV3.class, ArchiveTypes.MONTH);
		assertEquals(adr, 2890);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2020-01-01"), ArchiveV3.class, ArchiveTypes.MONTH);
		assertEquals(adr, 2076);

		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2016-01-01"), ArchiveV3.class, ArchiveTypes.DAY);
		assertEquals(adr, 3000);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2017-06-01"), ArchiveV3.class, ArchiveTypes.DAY);
		assertEquals(adr, 11680);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2020-07-01"), ArchiveV3.class, ArchiveTypes.DAY);
		assertEquals(adr, 3000);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2020-12-31"), ArchiveV3.class, ArchiveTypes.DAY);
		assertEquals(adr, 13360);

		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2016-01-01"), ArchiveV3.class, ArchiveTypes.HOUR);
		assertEquals(adr, 13500);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2017-06-01"), ArchiveV3.class, ArchiveTypes.HOUR);
		assertEquals(adr, 34332);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2020-07-01"), ArchiveV3.class, ArchiveTypes.HOUR);
		assertEquals(adr, 13500);
		adr=ArchiveFactory.convertDateToAddress(LocalDate.parse("2020-12-31"), ArchiveV3.class, ArchiveTypes.HOUR);
		assertEquals(adr, 54492);
	}
	
	void parseArchive(File file) throws Exception{
		IArchive archive = as.readArchiveFromFile(file);
		ICurrentData cd = archive.getCurrentData();
		DayArchive da = archive.getDayArchive();
		LocalDateTime dt = cd.getCurrentDateTime();
		da.getDayRecord(dt.toLocalDate().minusDays(1));
		MonthArchive ma = archive.getMonthArchive();
	}

}
