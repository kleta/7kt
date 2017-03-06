package ru.sevenkt.reader.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ru.sevenkt.reader.services.impl.BaudRates;
import ru.sevenkt.reader.services.impl.Reader;

public class ReaderTest {
	
	Reader reader=new Reader("COM3", 2400);

//	@Test
//	public void testConnect() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReadEEPROM() {
//		fail("Not yet implemented");
//	}

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

}
