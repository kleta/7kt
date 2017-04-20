package ru.sevenkt.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ru.sevenkt.domain.version4.ArchiveV4;

public class IArchiveTest {
	
	@Test
	public void testToFile() throws Exception{
		byte[] b=new byte[65536];
		b[0]=8;
		b[1]=4;
		b[2]=3;
		b[3]=7;
		b[80]=17;
		b[81]=1;
		b[82]=1;
		b[83]=1;
		b[84]=1;
		IArchive arch = ArchiveFactory.createArhive(b);
		assertTrue(arch instanceof ArchiveV4);
		arch.toFile("target");
	}

}
