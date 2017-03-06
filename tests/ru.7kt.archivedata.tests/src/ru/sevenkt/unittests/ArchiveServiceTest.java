package ru.sevenkt.unittests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ru.sevenkt.archive.services.impl.ArchiveServiceImpl;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.MonthArchive;

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
	
	public void testSpreadHourData() throws Exception{
		File file = new File("resources/V3/02016_2016-02-04_13-00.bin");
		IArchive archive = as.readArchiveFromFile(file);
	}
	
	void parseArchive(File file) throws Exception{
		IArchive archive = as.readArchiveFromFile(file);
		ICurrentData cd = archive.getCurrentData();
		DayArchive da = archive.getDayArchive();
		// IHourArchive ha = archive.getHourArchive();
		// IJournalSettings js = archive.getJournalSettings();
		MonthArchive ma = archive.getMonthArchive();
	}

}
