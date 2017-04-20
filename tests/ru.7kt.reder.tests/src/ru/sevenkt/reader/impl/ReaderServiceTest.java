package ru.sevenkt.reader.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.reader.services.Archive7KTC;
import ru.sevenkt.reader.services.IReaderService;
import ru.sevenkt.reader.services.impl.ReaderImpl;

public class ReaderServiceTest {

	private static final String port = "COM4";
	private IReaderService readerService = new ReaderImpl();

	@Test
	public void testReadFullArhive() throws Exception {
		Connection con = new Connection("", port, null, null);
		readerService.readFullArchive(con);
	}

	@Test
	public void readListArchiveFrom7KTC() throws Exception {
		List<Archive7KTC> archives = readerService.readListArchiveFrom7KTC(port, true);
		for (Archive7KTC archive7ktc : archives) {
			System.out.println(archive7ktc);
		}
	}
	@Test
	public void readListArchiveFrom7KTC1() throws Exception {
		int c = readerService.getArchiveCount(port, true);
		int adr = 0;
		List<Archive7KTC> list=new ArrayList<>();
		for (int i=0; i<c;i++) {
			Archive7KTC arch = readerService.readFirstPageArchive(port, adr);
			adr=arch.getFirstBlockNum()+arch.getPagesInBlock()*arch.getNumberOfBlock();
			list.add(arch);
		}
		for (Archive7KTC archive7ktc : list) {
			System.out.println(archive7ktc);
		}
	}

	@Test
	public void readArhiveCount() throws Exception {
		int c = readerService.getArchiveCount(port, false);
		System.out.println(c);
	}
}
