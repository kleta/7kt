package ru.sevenkt.archive.services;

import java.io.File;

import ru.sevenkt.domain.Archive;;;

public interface IArchiveService {
	
	Archive readArchiveFromFile(File file) throws Exception;
}
