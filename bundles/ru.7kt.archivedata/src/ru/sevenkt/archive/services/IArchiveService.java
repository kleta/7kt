package ru.sevenkt.archive.services;

import java.io.File;

import ru.sevenkt.domain.IArchive;;;

public interface IArchiveService {
	
	IArchive readArchiveFromFile(File file) throws Exception;
	
	
}
