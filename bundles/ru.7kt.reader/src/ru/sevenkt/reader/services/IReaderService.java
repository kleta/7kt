package ru.sevenkt.reader.services;

import java.util.List;
import java.util.Set;

import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.ISettings;

public interface IReaderService {

	Set<String> getAvailableSerialPorts();

	ISettings readCurrentSettings(Device device) throws Exception;

	ICurrentData readCurrentData(Device device) throws Exception;

	IArchive readFullArchive(Connection connection) throws Exception;

	List<Archive7KTC> readListArchiveFrom7KTC(String port, boolean withDeleted) throws Exception;

	int getArchiveCount(String port, boolean whithDeleted) throws Exception;

	Archive7KTC readFirstPageArchive(String port, int prevAddress) throws Exception;

	IArchive readArchive(Archive7KTC archive, String port) throws Exception;
}
