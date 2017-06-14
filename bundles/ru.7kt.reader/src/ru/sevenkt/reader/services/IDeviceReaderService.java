package ru.sevenkt.reader.services;

import java.time.LocalDate;
import java.util.Set;

import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.ISettings;

public interface IDeviceReaderService  {

	ISettings readCurrentSettings(Device device) throws Exception;


	IArchive readFullArchive(Connection connection) throws Exception;

	IArchive readArchive(LocalDate from, LocalDate to, Connection con, Set<ArchiveTypes> types, boolean isJournal) throws Exception;
	
	Set<String> getAvailableSerialPorts();

	IJournalSettings readJournal(Connection con) throws Exception;
}
