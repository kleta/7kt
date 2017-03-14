package ru.sevenkt.reader.services;

import java.io.File;
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
}
