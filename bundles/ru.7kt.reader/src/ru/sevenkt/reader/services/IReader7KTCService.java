package ru.sevenkt.reader.services;

import java.util.List;
import java.util.Set;

import ru.sevenkt.domain.IArchive;

public interface IReader7KTCService {

	Set<String> getAvailableSerialPorts();

	List<Archive7KTC> readListArchiveFrom7KTC(String port, boolean withDeleted) throws Exception;

	int getArchiveCount(String port, boolean whithDeleted) throws Exception;

	Archive7KTC readFirstPageArchive(String port, int prevAddress) throws Exception;

	IArchive readArchive(Archive7KTC archive, String port) throws Exception;
	
	void clear(String port) throws Exception;

}