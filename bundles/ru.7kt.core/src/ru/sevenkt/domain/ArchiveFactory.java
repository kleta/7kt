package ru.sevenkt.domain;

import ru.sevenkt.domain.version1.ArchiveV1;
import ru.sevenkt.domain.version3.ArchiveV3;
import ru.sevenkt.domain.version4.ArchiveV4;

public class ArchiveFactory {

	public static IArchive createArhive(byte[] data) throws Exception {
		byte lengthArchive = data[0];
		byte versionArchive = data[1];
		if (lengthArchive == 8 && versionArchive == 3)
			return new ArchiveV3(data);
		if (lengthArchive == 8 && versionArchive == 4)
			return new ArchiveV4(data);
		if (lengthArchive == 4 && versionArchive == 1)
			return new ArchiveV1(data);
		return null;
	}
	
	public static Class<? extends IArchive> getArchiveClass(byte[] data){
		byte lengthArchive = data[0];
		byte versionArchive = data[1];
		if (lengthArchive == 8 && versionArchive == 3)
			return ArchiveV3.class;
		if (lengthArchive == 8 && versionArchive == 4)
			return ArchiveV4.class;
		return null;
	}
}
