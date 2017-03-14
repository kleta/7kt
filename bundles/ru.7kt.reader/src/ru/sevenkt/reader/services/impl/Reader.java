package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.NRSerialPort;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;

public class Reader {

	private Logger logWrite = LoggerFactory.getLogger("TX");

	private Logger logRead = LoggerFactory.getLogger("RX");
	
	private Logger log = LoggerFactory.getLogger("Reader");

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
		log.info("Установка соединения с устройством");
		while (!b && i < 5) {
			serial = new NRSerialPort(portName, baudRate);
			serial.connect();
			b = setBoundRate(BaudRates.S9600);
			serial.disconnect();
			serial.setBaud(9600);
			serial.connect();
			b = setBoundRate(BaudRates.S9600);
			if (b){
				log.info("Cоединение установлено!");
				return true;
			}
			Thread.sleep(1000);
			serial.disconnect();
			i++;
		}
		log.info("Не удалось установить соединение");
		return false;
	}

	public byte[] readEEPROM(int devAddr, int blockSize) throws Exception {

		byte[] currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		int arhiveSize = clazz.getAnnotation(Length.class).value();
		byte[] arhiveData = new byte[arhiveSize];
		for (int i = 0; i < currentData.length; i++)
			arhiveData[i] = currentData[i];

		// Читаем месячный архив
		log.info("Чтение месячного архива");
		long monthAddr = clazz.getDeclaredField("monthArchive").getAnnotation(Address.class).value();
		int monthLength = clazz.getDeclaredField("monthArchive").getType().getAnnotation(Length.class).value();
		byte[] monthData = readArchive(devAddr, monthAddr, monthLength, blockSize);
		for (int i = 0; i < monthData.length; i++)
			arhiveData[(int) (i + monthAddr)] = monthData[i];
		// Читаем дневной архив
		log.info("Чтение суточноного архива");
		long dayAddr = clazz.getDeclaredField("dayArchive").getAnnotation(Address.class).value();
		int dayLength = clazz.getDeclaredField("dayArchive").getType().getAnnotation(Length.class).value();
		byte[] dayData = readArchive(devAddr, dayAddr, dayLength, blockSize);
		for (int i = 0; i < dayData.length; i++)
			arhiveData[(int) (i + dayAddr)] = dayData[i];

		// Читаем часовой архив
		log.info("Чтение часового архива");
		long hourAddr = clazz.getDeclaredField("hourArchive").getAnnotation(Address.class).value();
		int hourLength = clazz.getDeclaredField("hourArchive").getType().getAnnotation(Length.class).value();
		byte[] hourData = readArchive(devAddr, hourAddr, hourLength, blockSize);
		for (int i = 0; i < hourData.length; i++)
			arhiveData[(int) (i + hourAddr)] = hourData[i];
		
		// Читаем журнал установок
		log.info("Чтение журнала установок");
		long jAddr = clazz.getDeclaredField("journalSettings").getAnnotation(Address.class).value();
		int jLength = clazz.getDeclaredField("journalSettings").getType().getAnnotation(Length.class).value();
		byte[] jData = readArchive(devAddr, jAddr, jLength, blockSize);
		for (int i = 0; i < jData.length; i++)
			arhiveData[(int) (i + jAddr)] = jData[i];
		log.info("Архив считан. Получено "+arhiveData.length+" байт");
		return arhiveData;
	}

	public byte[] readArchive(int devAddr, long beginAddr, int archiveLength, int blockSize) throws Exception {
		byte[] archiveData = new byte[archiveLength];
		for (long addr = beginAddr; addr < beginAddr + archiveLength; addr += blockSize) {
			byte[] bytes = readData(devAddr, addr, blockSize);
			if (bytes != null) {
				for (int i = 0; i < bytes.length; i++) {
					if (addr - beginAddr + i < archiveData.length)
						archiveData[(int) (addr - beginAddr) + i] = bytes[i];
				}
			} else
				throw new Exception("Невозможно прочитать архив");
		}
		return archiveData;
	}

	private byte[] readData(int devAddr, long addr, int blockSize) throws Exception {
		CRC16 crc = new CRC16();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] readEEPROMCommand = Commands.getReadEEPROMCommand(devAddr, addr, blockSize);
		boolean vCRC;
		int i = 0;
		while (i < 10) {
			outs.write(readEEPROMCommand);
			Thread.sleep(300);
			logWrite.info("Length: " + readEEPROMCommand.length + ", Data: " + bytesToHex(readEEPROMCommand));
			byte[] bytes = new byte[blockSize + 2];
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			if (vCRC)
				return Arrays.copyOfRange(bytes, 0, blockSize);
			else {
				logRead.info("Ошибка CRC");
			}
			i++;
		}
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
				return Arrays.copyOfRange(bytes, 0, 194 - 2);
			else {
				logRead.info("Ошибка CRC");
			}
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
		else {
			logRead.info("Нет ответа от устройства");
		}
		return false;
	}

	public void disconnect() {
		log.info("Закрытие соединения");
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
