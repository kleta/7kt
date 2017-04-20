package ru.sevenkt.reader.services.impl;

import java.util.Arrays;

public class Commands {
	private static byte[] commandDevice={(byte) 0xA5,0,0,0,0,0,0,0};
	private static byte[] command7KTC={(byte) 0xA5,0,0,0,0,0};
	
	private static CRC16 crcCalc=new CRC16(); 

	public static byte[] getSetBaudRateCommand(int rate, int addr) {
		commandDevice[1]=(byte) addr;
		commandDevice[2]=6;
		commandDevice[3]=(byte) rate;
		commandDevice[4]=(byte) rate;
		commandDevice[5]=0;
		addCrcToDeviceCommand(commandDevice);
		return commandDevice;
	}
	public static byte[] getCurrentDataCommand(int addr) {
		commandDevice[1]=(byte) addr;
		commandDevice[2]=1;
		commandDevice[3]=0;
		commandDevice[4]=0;
		commandDevice[5]=(byte) 192;
		addCrcToDeviceCommand(commandDevice);
		return commandDevice;
	}

	
	
	private static void addCrcToDeviceCommand(byte[] cmd) {
		
		int crc = crcCalc.getCRC(Arrays.copyOfRange(cmd, 1, 6));
		byte crcL=(byte) (crc&0xFF);
		byte crcH=(byte) (crc>>8);
		cmd[6]=crcL;
		cmd[7]=crcH;
	}
	public static byte[] getReadEEPROMCommand(int devAddr, long addr, int blockSize) {
		commandDevice[1]=(byte) devAddr;
		commandDevice[2]=1;
		commandDevice[3]=(byte) (addr&0xFF);
		commandDevice[4]=(byte) (addr>>8);
		commandDevice[5]=(byte) blockSize;
		addCrcToDeviceCommand(commandDevice);
		return commandDevice;
	}
	public static byte[] getReadAttributesCommand() {
		command7KTC[1]=4;
		addCrcTo7KTCCommand(command7KTC);
		return command7KTC;
	}
	public static byte[] getReadPicCommand() {
		command7KTC[1]=2;
		addCrcTo7KTCCommand(command7KTC);
		return command7KTC;
	}
	private static void addCrcTo7KTCCommand(byte[] cmd) {
		int crc = crcCalc.getCRC(Arrays.copyOfRange(cmd, 1, 4));
		byte crcL=(byte) (crc&0xFF);
		byte crcH=(byte) (crc>>8);
		cmd[4]=crcL;
		cmd[5]=crcH;		
	}
	public static byte[] getReadBlockCommand(int num) {
		command7KTC[1]=5;
		command7KTC[2]=(byte) (num&0xFF);
		command7KTC[3]=(byte) (num>>8);		
		addCrcTo7KTCCommand(command7KTC);
		return command7KTC;
	}
}
