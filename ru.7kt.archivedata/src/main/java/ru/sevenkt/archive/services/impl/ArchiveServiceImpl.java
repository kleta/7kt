package ru.sevenkt.archive.services.impl;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ru.sevenkt.archive.domain.Archive;
import ru.sevenkt.archive.domain.Length;
import ru.sevenkt.archive.services.ArchiveService;

public class ArchiveServiceImpl implements ArchiveService {

	public Archive readArchiveFromFile(File file) throws Exception {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.skipBytes(64);
		byte[] data = new byte[Archive.class.getAnnotation(Length.class).value()];
		int ret = dis.read(data, 0, data.length);
		dis.close();
		return new Archive(data);
	}

}
