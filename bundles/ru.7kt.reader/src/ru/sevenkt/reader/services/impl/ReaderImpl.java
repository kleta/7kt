package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import gnu.io.NRSerialPort;
import ru.sevenkt.reader.services.IReaderService;

public class ReaderImpl implements IReaderService {

	private static final byte[] COMMAND_SET_BOUD_RATE_19200 = null;
	private static final byte[] COMMAND_SET_BOUD_RATE_2400 = null;

	@Override
	public Set<String> getAvailableSerialPorts() {
		return NRSerialPort.getAvailableSerialPorts();
	}

	@Override
	public File readFullArchive(String port) throws InterruptedException {

//		while(!device.connect()){
//			Thread.sleep(1000);
//		}
//		byte[] eeprom=device.readEEPROM();
//		byte[] curData=device.readCurrentData();
		return null;
	}

//	private void initConnection(String port) throws IOException {
//		int baudRate = 2400;
//		NRSerialPort serial = new NRSerialPort(port, baudRate);
//		serial.connect();
//		DataInputStream ins = new DataInputStream(serial.getInputStream());
//		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
//		outs.write(COMMAND_SET_BOUD_RATE_19200);
//		ins.read();
//	}
//
//	private void AddCRC(byte Byte) {
//		int i, Carry;
//		for (i = 0; i < 8; i++) {
//			Carry = 0;
//			if (CRC & 0x8000)
//				Carry = 1;
//			CRC <<= 1;
//			if (0x80 & Byte)
//				CRC |= 0x01; // задвинем бит
//			Byte <<= 1;
//			if (Carry == 1)
//				CRC ^= 0x1021;
//		}
//	}

}
