package ru.sevenkt.archive.services;

import java.io.File;
import java.io.IOException;

import ru.sevenkt.archive.domain.Archive;

public interface IArchiveService {
	
	Archive readArchiveFromFile(File file) throws Exception;
}
