package ru.sevenkt.reader.impl;

import org.junit.Test;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.reader.services.IReaderService;
import ru.sevenkt.reader.services.impl.ReaderImpl;

public class ReaderServiceTest {
	
	private IReaderService s=new ReaderImpl();
	
	@Test
	public void testReadFullArhive() throws Exception{
		s.readFullArchive(new Device());
	}
}
