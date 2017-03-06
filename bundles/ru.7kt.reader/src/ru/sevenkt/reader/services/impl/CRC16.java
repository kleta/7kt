package ru.sevenkt.reader.services.impl;

import java.util.Arrays;

public class CRC16 {
	int CRC = 1;

	public int getCRC(byte[] bytes) {
		CRC = 1;
		for (byte b : bytes) {
			addCRC(b);
		}
		addCRC((byte) 0);
		addCRC((byte) 0);
		return CRC&0xFFFF;
	}

	private void addCRC(byte b) {
		int i, carry;
		for (i = 0; i < 8; i++) {
			carry = 0;
			int j = CRC & 0x8000;
			if (j>0)
				carry = 1;
			CRC <<= 1;
			int j2 = 0x80 & b;
			if (j2>0){
				CRC |= 0x01; // задвинем бит
			}
			b <<= 1;
			if (carry>0){
				CRC ^= 0x1021;
			}
		}
	}
	
	public boolean validateCRC(byte[] bytes){
		int crc = getCRC(Arrays.copyOfRange(bytes, 0, bytes.length-2));
		int crcL = crc&0xFF;
		int crcH = crc>>8;
		if((byte)crcL==bytes[bytes.length-2] && (byte)crcH==bytes[bytes.length-1])
			return true;
		return false;
	}

}
