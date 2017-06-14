package ru.sevenkt.reader.services.impl;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gnu.io.NRSerialPort;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.annotations.RecordLength;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.version3.ArchiveV3;
import ru.sevenkt.domain.version4.ArchiveV4;
import ru.sevenkt.reader.services.IDeviceReaderService;

public class DeviceReaderImpl implements IDeviceReaderService {

	@Override
	public Set<String> getAvailableSerialPorts() {
		return NRSerialPort.getAvailableSerialPorts();
	}

	@Override
	public IArchive readFullArchive(Connection con) throws Exception {
		ReaderCOM reader = ReaderFactory.createReader(con);
		if (reader.connect()) {
			byte[] data = reader.readEEPROM(0, 256);
			reader.disconnect();
			return ArchiveFactory.createArhive(data);
		}
		return null;

	}

	@Override
	public IArchive readArchive(LocalDate from, LocalDate to, Connection con, Set<ArchiveTypes> types,
			boolean isJournal) throws Exception {
		Set<Integer> monthAddresses = new HashSet<>();
		Set<Integer> dayAddresses = new HashSet<>();
		Set<Integer> hourAddresses = new HashSet<>();
		ReaderCOM reader = ReaderFactory.createReader(con);
		int monthRecSize = 0;
		int dayRecSize = 0;
		int hourRecSize = 0;
		if (reader.connect()) {
			byte[] cd = reader.readCurrentData();
			Class<? extends IArchive> ac = ArchiveFactory.getArchiveClass(cd);
			LocalDate dt = from;
			Field monthArhiveField = ac.getDeclaredField("monthArchive");
			monthRecSize = monthArhiveField.getType().getAnnotation(RecordLength.class).value();
			Field dayArhiveField = ac.getDeclaredField("dayArchive");
			dayRecSize = dayArhiveField.getType().getAnnotation(RecordLength.class).value();
			Field hourArhiveField = ac.getDeclaredField("hourArchive");
			hourRecSize = hourArhiveField.getType().getAnnotation(RecordLength.class).value();
			while (dt.isBefore(to)) {
				for (ArchiveTypes type : types) {
					int address = ArchiveFactory.convertDateToAddress(dt, ac, type);
					switch (type) {
					case MONTH:
						if (dt.getDayOfMonth() == 1) {
							monthAddresses.add(address);
						}
						break;
					case DAY:
						dayAddresses.add(address);
						break;
					case HOUR:
						for (int i = 1; i < 25; i = i + 8) {
							hourAddresses.add(address + hourRecSize * i);
						}
						break;
					default:
						break;
					}
				}
				dt = dt.plusDays(1);
			}
			Map<Integer, byte[]> journalData = null;
			if (isJournal) {
				journalData=reader.readJournal(0, 256);
			}
			
				
			Map<Integer, byte[]> monthData = null;
			if (monthAddresses.size() > 0)
				monthData = reader.readData(0, monthRecSize, monthAddresses);
			Map<Integer, byte[]> dayData = null;
			if (dayAddresses.size() > 0)
				dayData = reader.readData(0, dayRecSize, dayAddresses);
			Map<Integer, byte[]> hourData = null;
			if (hourAddresses.size() > 0)
				hourData = reader.readData(0, hourRecSize * 8, hourAddresses);
			reader.disconnect();
			int arhiveSize = ac.getAnnotation(Length.class).value();
			byte[] arсhiveData = new byte[arhiveSize];
			Arrays.fill(arсhiveData, (byte) -1);
			if (cd != null)
				System.arraycopy(cd, 0, arсhiveData, 0, cd.length);
			if (monthData != null)
				reader.copyMapToArray(monthData, arсhiveData, 0);
			if (dayData != null)
				reader.copyMapToArray(dayData, arсhiveData, 0);
			if (hourData != null)
				reader.copyMapToArray(hourData, arсhiveData, 0);
			if(journalData!=null)
				reader.copyMapToArray(journalData, arсhiveData, 0);
			return ArchiveFactory.createArhive(arсhiveData);

		}
		return null;
	}

	@Override
	public ISettings readCurrentSettings(Device device) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IJournalSettings readJournal(Connection con) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
