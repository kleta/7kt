package ru.sevenkt.reader.services;

import java.io.File;
import java.util.Set;

public interface IReaderService {
	
	Set<String> getAvailableSerialPorts();
	
	File readFullArchive(String port) throws Exception;
}
