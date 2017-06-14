package ru.sevenkt.reader.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IDayRecord;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.IMonthRecord;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.reader.services.impl.BaudRates;
import ru.sevenkt.reader.services.impl.Reader;
import ru.sevenkt.reader.services.impl.ReaderCOM;

public class ReaderCableV3Test {
	
	ReaderCOM reader=new ReaderCOM("COM4", 2400, 300);



	@Test
	public void testReadCurrentData() throws Exception {
		byte[] b;
		if(reader.connect()){
			b=reader.readCurrentData();
			assertNotNull(b);
			reader.disconnect();
		}
		else{
			reader.disconnect();
			fail("Нет соединения с устройством");		
		}
	}

	@Test
	public void testSetBoundRate() throws Exception {
		boolean b = reader.connect();
		assertTrue(b);
		reader.disconnect();
	}

	
	
	@Test
	public void testReadMonthArchive() throws Exception {
		boolean b = reader.connect();
		byte[] cData = reader.readCurrentData();
		Class<? extends IArchive> ac = ArchiveFactory.getArchiveClass(cData);
		int arhiveSize = ac.getAnnotation(Length.class).value();
		byte[] arсhiveData = new byte[arhiveSize];
		Arrays.fill(arсhiveData, (byte) -1);
		Field monthArhiveField = ac.getDeclaredField("monthArchive");
		long monthAddr = monthArhiveField.getAnnotation(Address.class).value();
		byte[] bytes = reader.readMonhData(0, 256);
		reader.disconnect();
		System.arraycopy(bytes, 0, arсhiveData, (int) monthAddr, bytes.length);
		System.arraycopy(cData, 0, arсhiveData, 0, cData.length);		
		IArchive ar = ArchiveFactory.createArhive(arсhiveData);
		assertNotNull(ar);
		MonthArchive ma = ar.getMonthArchive();
		assertNotNull(ma);
		IMonthRecord rec = ma.getMonthRecord(LocalDate.now().withDayOfMonth(1));
		assertNotNull(rec);
		LocalDate dt=LocalDate.now().minusMonths(36).withDayOfMonth(1);
		while(dt.isBefore(LocalDate.now())){		
			IMonthRecord monthRecord = ma.getMonthRecord(dt);
			System.out.println(monthRecord);
			dt=dt.plusMonths(1);
		}
	}
	@Test
	public void testReadDayArchive() throws Exception {
		LocalDateTime from = LocalDateTime.now();
		boolean b = reader.connect();
		byte[] cData = reader.readCurrentData();
		Class<? extends IArchive> ac = ArchiveFactory.getArchiveClass(cData);
		int arhiveSize = ac.getAnnotation(Length.class).value();
		byte[] arсhiveData = new byte[arhiveSize];
		Arrays.fill(arсhiveData, (byte) -1);
		Field dayArhiveField = ac.getDeclaredField("dayArchive");
		long dayAddr = dayArhiveField.getAnnotation(Address.class).value();
		byte[] bytes = reader.readDayData(0, 256);
		reader.disconnect();
		LocalDateTime to = LocalDateTime.now();
		long min = ChronoUnit.MINUTES.between(from, to);
		long sec = ChronoUnit.SECONDS.between(from, to);
		if(min==0)
			min=1;
		System.out.println("Время чтения "+min+":"+(sec%min*60));
		System.arraycopy(bytes, 0, arсhiveData, (int) dayAddr, bytes.length);
		System.arraycopy(cData, 0, arсhiveData, 0, cData.length);		
		IArchive ar = ArchiveFactory.createArhive(arсhiveData);
		assertNotNull(ar);
		DayArchive da = ar.getDayArchive();
		assertNotNull(da);
		IDayRecord rec = da.getDayRecord(LocalDate.now());
		assertNotNull(rec);
		LocalDate dt=LocalDate.now().minusDays(186);
		while(dt.isBefore(LocalDate.now())){		
			IDayRecord dayRecord = da.getDayRecord(dt);
			System.out.println(dayRecord);
			dt=dt.plusDays(1);
		}
	}
	@Test
	public void testReadHourArchive() throws Exception {
		LocalDateTime from = LocalDateTime.now();
		boolean b = reader.connect();
		byte[] cData = reader.readCurrentData();
		Class<? extends IArchive> ac = ArchiveFactory.getArchiveClass(cData);
		int arhiveSize = ac.getAnnotation(Length.class).value();
		byte[] arсhiveData = new byte[arhiveSize];
		Arrays.fill(arсhiveData, (byte) -1);
		Field hourArhiveField = ac.getDeclaredField("hourArchive");
		long hourAddr = hourArhiveField.getAnnotation(Address.class).value();
		byte[] bytes = reader.readHourData(0, 256);
		reader.disconnect();
		LocalDateTime to = LocalDateTime.now();
		long sec = ChronoUnit.SECONDS.between(from, to);
		System.out.println("Время чтения "+sec);
		System.arraycopy(bytes, 0, arсhiveData, (int) hourAddr, bytes.length);
		System.arraycopy(cData, 0, arсhiveData, 0, cData.length);		
		IArchive ar = ArchiveFactory.createArhive(arсhiveData);
		assertNotNull(ar);
		HourArchive ha = ar.getHourArchive();
		assertNotNull(ha);
		List<IHourRecord> rec = ha.getRecordsByDay(LocalDate.now());
		assertNotNull(rec);
		LocalDate dt=LocalDate.now().minusDays(61);
		while(dt.isBefore(LocalDate.now())){		
			List<IHourRecord> records =ha.getRecordsByDay(dt);
			for (IHourRecord iHourRecord : records) {
				System.out.println(iHourRecord);
			}		
			dt=dt.plusDays(1);
		}
	}
	@Test
	public void testReadEEPROM() throws Exception {
		LocalDateTime from = LocalDateTime.now();
		boolean b = reader.connect();
		byte[] bytes = reader.readEEPROM(0, 256);
		LocalDateTime to = LocalDateTime.now();
		long sec = ChronoUnit.SECONDS.between(from, to);
		System.out.println("Время чтения "+sec);
		
		IArchive ar = ArchiveFactory.createArhive(bytes);
		assertNotNull(ar);
		HourArchive ha = ar.getHourArchive();
		assertNotNull(ha);
		List<IHourRecord> rec = ha.getRecordsByDay(LocalDate.now());
		assertNotNull(rec);
		LocalDate dt=LocalDate.now().minusDays(60);
		while(dt.isBefore(LocalDate.now())){		
			List<IHourRecord> records =ha.getRecordsByDay(dt);
			for (IHourRecord iHourRecord : records) {
				System.out.println(iHourRecord);
			}		
			dt=dt.plusDays(1);
		}
	}
	
}
