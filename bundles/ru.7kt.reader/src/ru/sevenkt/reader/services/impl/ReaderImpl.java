package ru.sevenkt.reader.services.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import gnu.io.NRSerialPort;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.ISettings;

import ru.sevenkt.reader.services.IReaderService;

public class ReaderImpl implements IReaderService {

	private Reader reader;

	@Override
	public Set<String> getAvailableSerialPorts() {
		return NRSerialPort.getAvailableSerialPorts();
	}

	@Override
	public IArchive readFullArchive(Connection con) throws Exception {
		reader = new Reader(con.getPort(), 2400);
		if (reader.connect()) {
			byte[] data = reader.readEEPROM(0, 256);
			reader.disconnect();
			return ArchiveFactory.createArhive(data);
		}
		return null;
		
	}

	@Override
	public ISettings readCurrentSettings(Device device) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICurrentData readCurrentData(Device device) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
