package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.NRSerialPort;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;

public class Reader {

	private Logger logWrite = LoggerFactory.getLogger("WRITE");

	private Logger logRead = LoggerFactory.getLogger("READ");

	private NRSerialPort serial;

	private String portName;

	private int baudRate;

	public Reader(String port, int rate) {
		portName = port;
		baudRate = rate;
	}

	public boolean connect() throws IOException, InterruptedException {
		boolean b = false;
		int i = 0;
		while (!b && i < 5) {
			serial = new NRSerialPort(portName, baudRate);
			serial.connect();
			b = setBoundRate(BaudRates.S9600);
			serial.disconnect();
			serial.setBaud(9600);
			serial.connect();
			b = setBoundRate(BaudRates.S9600);
			if (b)
				return true;
			Thread.sleep(1000);
			serial.disconnect();
			i++;
		}
		return false;
	}

	public byte[] readEEPROM() throws Exception {
		byte[] bytes = readCurrentData();
		IArchive clazz = ArchiveFactory.createArhive(bytes);
		
		return null;
	}

	public byte[] readCurrentData() throws Exception {
		CRC16 crc = new CRC16();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readCurrentDataCommand = Commands.getCurrentDataCommand(0);
		boolean vCRC;
		int i = 0;
		while (i < 10) {
			outs.write(readCurrentDataCommand);
			Thread.sleep(300);
			logWrite.info("Length: " + readCurrentDataCommand.length + ", Data: " + bytesToHex(readCurrentDataCommand));
			byte[] bytes = new byte[194];
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			if (vCRC)
				return bytes;
			i++;
		}
		return null;
	}

	public boolean setBoundRate(int rate) throws IOException {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] setBaudRateCommand = Commands.getSetBaudRateCommand(rate, 0);
		outs.write(setBaudRateCommand);
		logWrite.info("Length: " + setBaudRateCommand.length + ", Data: " + bytesToHex(setBaudRateCommand));
		int b = ins.read();
		logRead.info("Length: " + 1 + ", Data: " + b);
		if (b == 6)
			return true;
		return false;
	}

	public void disconnect() {
		serial.disconnect();
	}

	final protected static char[] hexArray = "0123456789ABCDEF ".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 3];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = hexArray[16];
		}
		return new String(hexChars);
	}
}
