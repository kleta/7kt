package ru.sevenkt.reader.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.IMonthRecord;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.version3.CurrentDataV3;
import ru.sevenkt.domain.version3.MonthArchiveV3;
import ru.sevenkt.reader.services.impl.ReaderModem;

public class ReaderModemTest {

	static ReaderModem reader;

	@BeforeClass
	public static void init() {
		Connection con = new Connection("", "COM3", "89503047832", "");
		reader = new ReaderModem(con, 1200);
	}

	@Test
	public void connect() throws Exception {
		byte[] bc = new byte[] { 0x0D };
		System.out.println(new String(bc));
		boolean b = reader.connect();
		assertTrue(b);

	}

	@Test
	public void readMonth() throws Exception {
		byte[] bc = new byte[] { 0x0D };
		System.out.println(new String(bc));
		boolean b = reader.connect();
		if (b)
			reader.readMonthArchive(0, 256);

	}
	@Test
	public void readDay() throws Exception {
		byte[] bc = new byte[] { 0x0D };
		System.out.println(new String(bc));
		boolean b = reader.connect();
		if (b)
			reader.readDayArchive(0, 256);
		
	}
	@Test
	public void readHour() throws Exception {
		byte[] bc = new byte[] { 0x0D };
		System.out.println(new String(bc));
		boolean b = reader.connect();
		if (b)
			reader.readHourArchive(0, 256);
		
	}
	@Test
	public void readEEPROM() throws Exception {
		boolean b = reader.connect();
		byte[] data = null;
		LocalDateTime st=LocalDateTime.now();
		if (b)
			data=reader.readEEPROM(0, 256);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH");
		File file = new File("modem-data"+formatter.format(st)+".bin");
		FileUtils.writeByteArrayToFile(file, data);
		System.out.println(file.getAbsolutePath());
		LocalDateTime end=LocalDateTime.now();
		long s = ChronoUnit.SECONDS.between(st, end);
		System.out.println(s/60);
		IArchive arh = ArchiveFactory.createArhive(data);
		MonthArchive ma = arh.getMonthArchive();
		DayArchive da = arh.getDayArchive();
		HourArchive ha = arh.getHourArchive();
		LocalDateTime dt = end.minusMonths(6);
		while(dt.isBefore(end)){
			System.out.println(ma.getMonthRecord(dt.toLocalDate()));
			dt=dt.plusMonths(1);
		}
		dt = end.minusMonths(6);
		while(dt.isBefore(end)){
			System.out.println(da.getDayRecord(dt.toLocalDate()).toString());
			dt=dt.plusDays(1);
		}
		dt = end.minusMonths(2);
		while(dt.isBefore(end)){
			List<IHourRecord> recordsByDay = ha.getRecordsByDay(dt.toLocalDate());
			System.out.println(dt);
			for (IHourRecord iHourRecord : recordsByDay) {
				System.out.println(iHourRecord.getDateTime()+iHourRecord.toString());
			}
			dt=dt.plusDays(1);
		}
		//System.out.println(data);
		
	}
	
	@Test
	public void analyzeFile() throws Exception{
		LocalDateTime end=LocalDateTime.now();
		byte[] data = FileUtils.readFileToByteArray(new File("modem-data2017-05-17_11.bin"));
		IArchive arh = ArchiveFactory.createArhive(data);
		MonthArchive ma = arh.getMonthArchive();
		DayArchive da = arh.getDayArchive();
		HourArchive ha = arh.getHourArchive();
		LocalDateTime dt = end.minusMonths(6);
		LocalDate localDate = dt.toLocalDate();
		while(dt.isBefore(end)){
			IMonthRecord monthRecord = ma.getMonthRecord(localDate);
			System.out.println(monthRecord);
			localDate=localDate.plusMonths(1);
		}
		dt = end.minusMonths(6);
		while(dt.isBefore(end)){
			System.out.println(da.getDayRecord(localDate));
			dt=dt.plusDays(1);
		}
		dt = end.minusMonths(2);
		while(dt.isBefore(end)){
			List<IHourRecord> recordsByDay = ha.getRecordsByDay(localDate);
			System.out.println(dt);
			for (IHourRecord iHourRecord : recordsByDay) {
				System.out.println(iHourRecord);
			}
			dt=dt.plusDays(1);
		}
	}

}
