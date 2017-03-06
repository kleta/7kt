package ru.sevenkt.archive.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;

public class ArchiveServiceImpl implements IArchiveService {

	public IArchive readArchiveFromFile(File file) throws Exception {
		byte[] data = FileUtils.readFileToByteArray(file);
		data=Arrays.copyOfRange(data, 64, data.length);
		List list=new ArrayList<>();
		Stream a = list.stream();	
		return ArchiveFactory.createArhive(data); 
	}

}
