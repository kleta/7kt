package ru.sevenkt.reader.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ru.sevenkt.reader.services.impl.BaudRates;
import ru.sevenkt.reader.services.impl.Reader;
import ru.sevenkt.reader.services.impl.ReaderCOM;

public class ReaderCableV3Test {
	
	ReaderCOM reader=new ReaderCOM("COM3", 2400, 300);



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
	public void testReadEEPROM() throws Exception {
		boolean b = reader.connect();
		byte[] bytes = reader.readEEPROM(0,256);
		reader.disconnect();
	}
	
	@Test
	public void testReadMonthArchive() throws Exception {
		boolean b = reader.connect();
		byte[] bytes = reader.readArchive(0, 300, 2664, 256);
		reader.disconnect();
	}
	@Test
	public void testReadDayArchive() throws Exception {
		boolean b = reader.connect();
		byte[] bytes = reader.readArchive(0, 3000, 10416, 256);
		reader.disconnect();
	}
	@Test
	public void testReadHourArchive() throws Exception {
		boolean b = reader.connect();
		byte[] bytes = reader.readArchive(0, 13500, 41664, 256);
		reader.disconnect();
	}
	@Test
	public void testReadJournalSettings() throws Exception {
		boolean b = reader.connect();
		byte[] bytes = reader.readArchive(0, 55500, 2560, 256);
		reader.disconnect();
	}
	
}
