package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;
import ru.sevenkt.utils.HexUtils;

public class Reader7KTC {

	protected CRC16 crc = new CRC16();
	protected Logger logWrite = LoggerFactory.getLogger("TX");
	protected Logger logRead = LoggerFactory.getLogger("RX");
	protected Logger log = LoggerFactory.getLogger("Reader");
	protected NRSerialPort serial;

	public Reader7KTC(String port) {
		serial = new NRSerialPort(port, 38400);
	}

	public byte[] readPage(int address) throws Exception {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readBlockCommands = Commands.getReadBlockCommand(address);
		boolean vCRC;
		int i = 0;
		while (i < 10) {
			outs.write(readBlockCommands);
			logWrite.info("Length: " + readBlockCommands.length + ", Data: " + HexUtils.bytesToHex(readBlockCommands));
			byte[] bytes = new byte[516];
			Thread.sleep(300);
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + HexUtils.bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			vCRC = crc.validateCRC(Arrays.copyOfRange(bytes, 0, 514));
			if (vCRC)
				return Arrays.copyOfRange(bytes, 0, 512);
			else {
				logRead.info("Ошибка CRC");
			}
			i++;
		}
		return null;
	}

	public byte[] readAttributes() throws Exception {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readAttributeCommands = Commands.getReadAttributesCommand();
		boolean vCRC;
		int i = 0;
		while (i < 10) {
			outs.write(readAttributeCommands);
			logWrite.info("Length: " + readAttributeCommands.length + ", Data: "
					+ HexUtils.bytesToHex(readAttributeCommands));
			byte[] bytes = new byte[66];
			Thread.sleep(100);
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + HexUtils.bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			if (vCRC)
				return Arrays.copyOfRange(bytes, 0, 64);
			else {
				logRead.info("Ошибка CRC");
			}
			i++;
		}
		return null;
	}

	public byte[] readPic() throws Exception {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readPicCommands = Commands.getReadPicCommand();
		boolean vCRC;
		int i = 0;
		while (i < 10) {
			outs.write(readPicCommands);
			logWrite.info("Length: " + readPicCommands.length + ", Data: " + HexUtils.bytesToHex(readPicCommands));
			byte[] bytes = new byte[4];
			Thread.sleep(100);
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + HexUtils.bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			if (vCRC)
				return Arrays.copyOfRange(bytes, 0, 64);
			else {
				logRead.info("Ошибка CRC");
			}
			i++;
		}
		return null;
	}

	public Map<Integer, byte[]> getArchivesList(boolean withDeleted, byte[] attributes, byte[] pic) throws Exception {
		Map<Integer, byte[]> map = new HashMap<>();
		int startBlock = 0;
		if (withDeleted) {
			for (int i = 0; i < attributes.length; i++) {
				if (attributes[i] == 1) {
					byte[] bytes = readPage(startBlock);
					map.put(startBlock, bytes);
				}
				if (attributes[i] == 2) {
					byte[] bytes = readPage(startBlock);
					map.put(startBlock, bytes);
					startBlock += 64;
					continue;
				}
				startBlock += 64;
			}
		}

		return map;
	}

	public boolean connect() {
		return serial.connect();
	}

	public void disconnect() {
		serial.disconnect();
	}

	public int getArchiveCount(boolean whithDeleted, byte[] pic, byte[] attr) throws Exception {
		int count = 0;
		for (byte b : attr) {
			if (b == 1 || b == 2)
				count++;
			if (!whithDeleted && count >= pic[0] && count > 0) {
				count--;
				break;
			}
		}
		return count;
	}

	public void clear() throws Exception {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readPicCommands = Commands.getClearPicCommand();
		int i = 0;
		while (i < 10) {
			outs.write(readPicCommands);
			logWrite.info("Length: " + readPicCommands.length + ", Data: " + HexUtils.bytesToHex(readPicCommands));
			byte[] bytes = new byte[4];
			Thread.sleep(100);
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + HexUtils.bytesToHex(bytes));
			if (bytes[0] != 0x6) {
				logRead.info("Ошибка");
				i++;
			}
			else
				break;
		}
	}
}
