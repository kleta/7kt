package ru.sevenkt.reader.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.sevenkt.reader.services.impl.CRC16;

public class CRC16Test {
	CRC16 c=new CRC16();
	
	@Test
	public void testGetCRC7kt(){
		byte[] b={0,6,6,6,0};
		int crc = c.getCRC(b);
		assertEquals(crc, 31295);
	}
}
