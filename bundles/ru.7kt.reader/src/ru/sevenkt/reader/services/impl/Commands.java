package ru.sevenkt.reader.services.impl;

import java.util.Arrays;

public class Commands {
	private static byte[] command={(byte) 0xA5,0,0,0,0,0,0,0};
	
	private static CRC16 crcCalc=new CRC16(); 

	public static byte[] getSetBaudRateCommand(int rate, int addr) {
		command[1]=(byte) addr;
		command[2]=6;
		command[3]=(byte) rate;
		command[4]=(byte) rate;
		command[5]=0;
		addCrc(command);
		return command;
	}
	public static byte[] getCurrentDataCommand(int addr) {
		command[1]=(byte) addr;
		command[2]=1;
		command[3]=0;
		command[4]=0;
		command[5]=(byte) 192;
		addCrc(command);
		return command;
	}

	
	
	private static void addCrc(byte[] cmd) {
		
		int crc = crcCalc.getCRC(Arrays.copyOfRange(cmd, 1, 6));
		String s = Integer.toBinaryString(crc);
		byte crcL=(byte) (crc&0xFF);
		s=Integer.toBinaryString(crcL);
		byte crcH=(byte) (crc>>8);
		s=Integer.toBinaryString(crcL);
		cmd[6]=crcL;
		cmd[7]=crcH;
	}
}
