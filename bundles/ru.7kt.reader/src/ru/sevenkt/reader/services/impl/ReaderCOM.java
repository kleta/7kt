package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.NRSerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.utils.HexUtils;

public class ReaderCOM extends Reader {
	CRC16 crc = new CRC16();
	private int baudRate;
	private int timeOut;
	private byte[] currentData;
	boolean CRCError = false;

	public ReaderCOM(String port, int rate, int i) {
		super(port);
		baudRate = rate;
		timeOut = i;
	}

	public boolean connect() throws IOException, InterruptedException, UnsupportedCommOperationException {
		boolean b = false;
		int i = 0;
		log.info("Установка соединения с устройством по " + portName);
		while (!b && i < 5) {
			if (serial == null)
				serial = new NRSerialPort(portName, baudRate);
			serial.connect();
			b = setBaudRate(BaudRates.S9600);
			serial.disconnect();
			serial.setBaud(9600);
			serial.connect();
			b = setBaudRate(BaudRates.S9600);
			if (b) {
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

	@Override
	public byte[] readEEPROM(int devAddr, int blockSize) throws Exception {
		currentData = readCurrentData();

		Map<Integer, byte[]> monthMapData = readMonthArchive(devAddr, blockSize);
		Map<Integer, byte[]> dayMapData = readDayArchive(devAddr, blockSize);
		Map<Integer, byte[]> hourMapData = readHourArchive(devAddr, blockSize);
		Map<Integer, byte[]> journalData = readJournal(devAddr, blockSize);
		if (currentData == null)
			return new byte[0];
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		int arhiveSize = clazz.getAnnotation(Length.class).value();
		byte[] arсhiveData = new byte[arhiveSize];
		Arrays.fill(arсhiveData, (byte) -1);
		if (currentData != null)
			System.arraycopy(currentData, 0, arсhiveData, 0, currentData.length);
		if (monthMapData != null)
			copyMapToArray(monthMapData, arсhiveData, 0);
		if (dayMapData != null)
			copyMapToArray(dayMapData, arсhiveData, 0);
		if (hourMapData != null)
			copyMapToArray(hourMapData, arсhiveData, 0);
		if (journalData != null)
			copyMapToArray(journalData, arсhiveData, 0);
		return arсhiveData;
	}

	void copyMapToArray(Map<Integer, byte[]> map, byte[] arсhiveData, int shift) {
		for (int adr : map.keySet()) {
			byte[] src = map.get(adr);
			System.arraycopy(src, 0, arсhiveData, adr - shift, src.length);
		}

	}

	Map<Integer, byte[]> readJournal(int devAddr, int blockSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);

		log.info("Чтение журнала установок. Размер блока " + blockSize);
		Field journalField;
		try {
			journalField = clazz.getDeclaredField("journalSettings");
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		long journalAddr = journalField.getAnnotation(Address.class).value();
		int journalLength = journalField.getType().getAnnotation(Length.class).value();
		Set<Integer> adresses = new HashSet<>();
		for (long adr = journalAddr; adr < journalAddr + journalLength; adr += blockSize) {
			adresses.add((int) adr);
		}
		Map<Integer, byte[]> journalData = readData(devAddr, blockSize, adresses);
		if (journalData == null) {
			log.info("Не удалось прочитать журнал установок");
		}
		return journalData;
	}

	public Map<Integer, byte[]> readMonthArchive(int devAddr, int blockSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);

		log.info("Чтение месячного архива. Размер блока " + blockSize);
		Field monthArhiveField = clazz.getDeclaredField("monthArchive");
		long monthAddr = monthArhiveField.getAnnotation(Address.class).value();
		int monthLength = monthArhiveField.getType().getAnnotation(Length.class).value();
		Set<Integer> adresses = new HashSet<>();
		for (long adr = monthAddr; adr < monthAddr + monthLength; adr += blockSize) {
			adresses.add((int) adr);
		}
		Map<Integer, byte[]> monthMapData = readData(devAddr, blockSize, adresses);
		if (monthMapData == null) {
			log.info("Не удалось прочитать месячный архив");
		}
		return monthMapData;
	}

	public Map<Integer, byte[]> readDayArchive(int devAddr, int blockSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);

		log.info("Чтение дневного архива. Размер блока " + blockSize);
		Field dayArhiveField = clazz.getDeclaredField("dayArchive");
		long dayAddr = dayArhiveField.getAnnotation(Address.class).value();
		int dayLength = dayArhiveField.getType().getAnnotation(Length.class).value();
		Set<Integer> adresses = new HashSet<>();
		for (long adr = dayAddr; adr < dayAddr + dayLength; adr += blockSize) {
			adresses.add((int) adr);
		}
		Map<Integer, byte[]> dayMapData = readData(devAddr, blockSize, adresses);
		if (dayMapData == null) {
			log.info("Не удалось прочитать дневной архив");
		}
		return dayMapData;
	}

	public Map<Integer, byte[]> readHourArchive(int devAddr, int blockSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);

		log.info("Чтение часового архива. Размер блока " + blockSize);
		Field hourArhiveField = clazz.getDeclaredField("hourArchive");
		long hourAddr = hourArhiveField.getAnnotation(Address.class).value();
		int hourLength = hourArhiveField.getType().getAnnotation(Length.class).value();
		Set<Integer> adresses = new HashSet<>();
		for (long adr = hourAddr; adr < hourAddr + hourLength; adr += blockSize) {
			adresses.add((int) adr);
		}
		Map<Integer, byte[]> hourMapData = readData(devAddr, blockSize, adresses);
		if (hourMapData == null) {
			log.info("Не удалось прочитать дневной архив");
		}
		return hourMapData;
	}

	public Map<Integer, byte[]> readData(int devAddr, int blockSize, Set<Integer> addresses)
			throws TooManyListenersException, IOException, InterruptedException {
		HashMap<Integer, byte[]> map = new HashMap<>();

		serial.addEventListener(new SerialPortEventListener() {
			private byte[] readBuffer = new byte[blockSize + 4];

			@Override
			public void serialEvent(SerialPortEvent ev) {
				if (!CRCError)
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				switch (ev.getEventType()) {
				case SerialPortEvent.DATA_AVAILABLE:
					try {
						readSerial();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

			}

			private void readSerial() throws Exception {
				InputStream inStream = serial.getInputStream();

				inStream.read(readBuffer, 0, blockSize + 4);
				boolean vCRC = crc.validateCRC(readBuffer);
				if (vCRC) {
					int adrL = readBuffer[0];
					int adrH = readBuffer[1];
					int l8 = (adrH & 0xff) << 8;
					int adr = l8 | (adrL & 0xff);
					byte[] data = Arrays.copyOfRange(readBuffer, 2, blockSize + 2);
					map.put(adr, data);
					System.out.println("Вставлен адрес " + adr);
					logRead.info("Length: " + readBuffer.length + ", Data: " + HexUtils.bytesToHex(readBuffer));

				} else {
					logRead.info("Ошибка CRC: " + readBuffer.length + "-" + HexUtils.bytesToHex(readBuffer));
					CRCError = true;
				}

			}
		});
		serial.notifyOnDataAvailable(true);

		OutputStream outStream = serial.getOutputStream();

		for (int i = 0; i < 20; i++) {
			int countCommands = addresses.size() - map.keySet().size();
			for (int adr : addresses) {
				if (map.get(adr) == null) {
					byte[] command = Commands.getReadEEPROMCommand16(devAddr, adr, blockSize);
					if (CRCError) {
						log.info("Sleep CRC");
						Thread.sleep(2000);
						CRCError = false;
					}
					logWrite.info("Length: " + command.length + ", Data: " + HexUtils.bytesToHex(command));
					outStream.write(command);
					int millis = (int) (3 * countCommands);
					if (millis < 300)
						millis = 300;
					Thread.sleep(millis);
				}

			}
			if (validateMap(map, addresses)) {
				serial.removeEventListener();
				return map;
			}
			log.info(
					"Цикл чтения " + (i + 1) + ": Прочитано " + map.keySet().size() + " блоков из " + addresses.size());
		}
		serial.removeEventListener();
		return null;
	}

	private boolean validateMap(HashMap<Integer, byte[]> mapData, Set<Integer> adresses) {
		for (int adr : adresses) {
			if (mapData.get(adr) == null)
				return false;
		}
		return true;
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
			logWrite.info("Length: " + readCurrentDataCommand.length + ", Data: "
					+ HexUtils.bytesToHex(readCurrentDataCommand));
			Thread.sleep(timeOut);
			byte[] bytes = new byte[130];
			int b = ins.read(bytes);
			logRead.info("Length: " + b + ", Data: " + HexUtils.bytesToHex(bytes));
			vCRC = crc.validateCRC(bytes);
			if (vCRC)
				return Arrays.copyOfRange(bytes, 0, 130 - 2);
			else {
				logRead.info("Ошибка CRC");
			}
			i++;
		}
		return null;
	}

	public boolean setBaudRate(int rate) throws IOException, InterruptedException {
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] setBaudRateCommand = Commands.getSetBaudRateCommand(rate, 0);
		outs.write(setBaudRateCommand);
		logWrite.info("Length: " + setBaudRateCommand.length + ", Data: " + HexUtils.bytesToHex(setBaudRateCommand));
		Thread.sleep(300);
		int b = ins.read();
		logRead.info("Length: " + 1 + ", Data: " + b);
		if (b == 6)
			return true;
		else {
			logRead.info("Нет ответа от устройства");
		}
		return false;
	}

	public void disconnect() throws IOException, InterruptedException {
		log.info("Закрытие соединения");
		serial.disconnect();
	}

	@Override
	public byte[] readMonhData(int devAdr, int blokSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		Field monthArhiveField = clazz.getDeclaredField("monthArchive");
		int monthLength = monthArhiveField.getType().getAnnotation(Length.class).value();
		long monthAddr = monthArhiveField.getAnnotation(Address.class).value();
		Map<Integer, byte[]> data = readMonthArchive(devAdr, blokSize);
		byte[] bytes = new byte[monthLength + blokSize];
		copyMapToArray(data, bytes, (int) monthAddr);
		return bytes;
	}

	@Override
	public byte[] readDayData(int devAdr, int blokSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		Field dayArhiveField = clazz.getDeclaredField("dayArchive");
		int dayLength = dayArhiveField.getType().getAnnotation(Length.class).value();
		long dayAddr = dayArhiveField.getAnnotation(Address.class).value();
		Map<Integer, byte[]> data = readDayArchive(devAdr, blokSize);
		byte[] bytes = new byte[dayLength + blokSize];
		copyMapToArray(data, bytes, (int) dayAddr);
		return bytes;
	}

	@Override
	public byte[] readHourData(int devAdr, int blokSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		Field hourArhiveField = clazz.getDeclaredField("hourArchive");
		int hourLength = hourArhiveField.getType().getAnnotation(Length.class).value();
		long hourAddr = hourArhiveField.getAnnotation(Address.class).value();
		Map<Integer, byte[]> data = readHourArchive(devAdr, blokSize);
		byte[] bytes = new byte[hourLength + blokSize];
		copyMapToArray(data, bytes, (int) hourAddr);
		return bytes;
	}

	@Override
	public byte[] readJournalData(int devAdr, int blokSize) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] readCurrentData(int devAdr, int blokSize) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
