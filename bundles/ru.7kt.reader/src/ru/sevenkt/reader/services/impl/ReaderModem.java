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

import org.apache.commons.lang3.ArrayUtils;

import gnu.io.NRSerialPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.utils.HexUtils;

public class ReaderModem extends ReaderCOM {

	private Connection connection;
	CRC16 crc = new CRC16();
	private Map<Integer, byte[]> monthMapData;
	private Map<Integer, byte[]> dayMapData;
	private Map<Integer, byte[]> hourMapData;
	private byte[] currentData;
	private Map<Integer, byte[]> journalData;

	public ReaderModem(Connection con, int timeOut) {
		super(con.getPort(), 9600, timeOut);
		this.connection = con;
	}

	@Override
	public boolean connect() throws IOException, InterruptedException, UnsupportedCommOperationException {
		serial = new NRSerialPort(portName, 9600);
		boolean connect = serial.connect();

		if (connect && ath0()) {
			boolean b;
			if (atd(connection.getPhone())) {
				return true;
			}

		}
		return false;
	}

	private boolean ath0() throws IOException, InterruptedException {
		byte[] command = new String("ATH0\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());

		byte[] bytes = new byte[20];
		int count = 0;
		while (count < 3) {
			logWrite.info("Length: " + command.length + ", Data: " + new String(command).replace("\r", "."));
			outs.write(command);
			Thread.sleep(500);
			int b = ins.read(bytes);
			if (b > 0) {
				String string = new String(bytes);
				logRead.info("Length: " + b + ", Data: " + string.replaceAll("\\r|\\n", "."));
				if (string.contains("OK"))
					return true;
			}
			count++;
		}
		log.info("Модем не отвечает");
		return false;
	}

	private boolean atd(String phone) throws IOException, InterruptedException {
		byte[] command = new String("ATD" + phone + "\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		byte[] bytes = new byte[194];
		int count = 0;
		while (count < 3) {
			logWrite.info("Length: " + command.length + ", Data: " + new String(command).replace("\r", "."));
			outs.write(command);
			Thread.sleep(20000);
			int b = ins.read(bytes);
			if (b > 0) {
				String string = new String(bytes);
				logRead.info("Length: " + b + ", Data: " + string.replaceAll("\\r|\\n", "."));
				if (string.contains("CONNECT"))
					return true;
			}
			count++;
		}
		return false;
	}

	private void escape() throws IOException, InterruptedException {
		byte[] command = new String("+++\r").getBytes();
		DataInputStream ins = new DataInputStream(serial.getInputStream());
		DataOutputStream outs = new DataOutputStream(serial.getOutputStream());
		outs.write(command);
		Thread.sleep(2000);
		byte[] bytes = new byte[10];
		int b = ins.read(bytes);
	}

	@Override
	public void disconnect() throws IOException, InterruptedException {
		escape();
		ath0();
		super.disconnect();
	}

	@Override
	public byte[] readEEPROM(int devAddr, int blockSize) throws Exception {
		currentData = readCurrentData();

		readMonthArchive(devAddr, blockSize);
		readDayArchive(devAddr, blockSize);
		readHourArchive(devAddr, blockSize);
		readJournal(devAddr, blockSize);
		if(currentData==null)
			return new byte[0];
		serial.disconnect();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);
		int arhiveSize = clazz.getAnnotation(Length.class).value();
		byte[] arсhiveData = new byte[arhiveSize];
		Arrays.fill(arсhiveData, (byte) -1);
		if (currentData != null)
			System.arraycopy(currentData, 0, arсhiveData, 0, currentData.length);
		if(monthMapData!=null)
			copyMapToArray(monthMapData, arсhiveData);
		if(dayMapData!=null)
			copyMapToArray(dayMapData, arсhiveData);
		if(hourMapData!=null)
			copyMapToArray(hourMapData, arсhiveData);
		if(journalData!=null)
			copyMapToArray(journalData, arсhiveData);			
		return arсhiveData;
	}

	private void copyMapToArray(Map<Integer, byte[]> map, byte[] arсhiveData) {
		for(int adr:map.keySet()){
			System.out.println(map.get(adr));
			System.arraycopy(map.get(adr), 0, arсhiveData, adr, map.get(adr).length);
		}
		
	}

	private void readJournal(int devAddr, int blockSize) throws Exception {
		if (currentData == null)
			currentData = readCurrentData();
		Class<? extends IArchive> clazz = ArchiveFactory.getArchiveClass(currentData);

		log.info("Чтение журнала установок. Размер блока " + blockSize);
		Field journalField;
		try {
			journalField = clazz.getDeclaredField("journalSettings");
		} catch (NoSuchFieldException | SecurityException e) {
			return;
		}
		long journalAddr = journalField.getAnnotation(Address.class).value();
		int journalLength = journalField.getType().getAnnotation(Length.class).value();
		Set<Integer> adresses = new HashSet<>();
		for (long adr = journalAddr; adr < journalAddr + journalLength; adr += blockSize) {
			adresses.add((int) adr);
		}
		journalData = readData(devAddr, blockSize, adresses);
		if (journalData == null) {
			log.info("Не удалось прочитать журнал установок");
		}

	}

	public void readMonthArchive(int devAddr, int blockSize) throws Exception {
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
		monthMapData = readData(devAddr, blockSize, adresses);
		if (monthMapData == null) {
			log.info("Не удалось прочитать месячный архив");
		}
	}

	public void readDayArchive(int devAddr, int blockSize) throws Exception {
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
		dayMapData = readData(devAddr, blockSize, adresses);
		if (dayMapData == null) {
			log.info("Не удалось прочитать дневной архив");
		}
	}

	public void readHourArchive(int devAddr, int blockSize) throws Exception {
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
		hourMapData = readData(devAddr, blockSize, adresses);
		if (hourMapData == null) {
			log.info("Не удалось прочитать дневной архив");
		}
	}

	private Map<Integer, byte[]> readData(int devAddr, int blockSize, Set<Integer> addresses)
			throws TooManyListenersException, IOException, InterruptedException {
		HashMap<Integer, byte[]> map = new HashMap<>();
		serial.addEventListener(new SerialPortEventListener() {
			private byte[] readBuffer = new byte[blockSize + 3];

			@Override
			public void serialEvent(SerialPortEvent ev) {
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

				inStream.read(readBuffer, 0, blockSize + 3);
				boolean vCRC = crc.validateCRC(readBuffer);
				if (vCRC) {
					int adrL = readBuffer[0];
					int adrH = readBuffer[1];
					int l8 = (adrH & 0xff) << 8;
					int adr = l8 | (adrL & 0xff);
					byte[] data = Arrays.copyOfRange(readBuffer, 2, blockSize - 1);
					map.put(adr, data);
					System.out.println("Вставлен адрес " + adr);
					logRead.info("Length: " + readBuffer.length + ", Data: " + HexUtils.bytesToHex(readBuffer));

				} else {
					logRead.info("Ошибка CRC: " + readBuffer.length + "-" + HexUtils.bytesToHex(readBuffer));
				}

			}
		});
		serial.notifyOnDataAvailable(true);

		OutputStream outStream = serial.getOutputStream();

		for (int i = 0; i < 20; i++) {
			int countCommands = addresses.size() - map.keySet().size();
			for (int adr : addresses) {
				if (map.get(adr) == null) {
					byte[] command = Commands.getReadEEPROMCommand16(devAddr, adr, blockSize - 1);
					logWrite.info("Length: " + command.length + ", Data: " + HexUtils.bytesToHex(command));
					outStream.write(command);
					int millis = (int) (3 * countCommands);
					if (millis < 300)
						millis = 300;
					Thread.sleep(millis);
				}

			}
			int millis = countCommands * 50;
			if (millis < 2000)
				millis = 2000;
			log.info("Sleep " + millis);
			Thread.sleep(millis);
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

	private void initMap(int blockSize) {
		monthMapData = new HashMap<Integer, byte[]>();
		dayMapData = new HashMap<Integer, byte[]>();
		hourMapData = new HashMap<Integer, byte[]>();
	}

}
