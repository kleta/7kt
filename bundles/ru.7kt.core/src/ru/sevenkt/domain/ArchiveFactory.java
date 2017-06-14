package ru.sevenkt.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Month;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.RecordLength;
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

	public static Class<? extends IArchive> getArchiveClass(byte[] data) {
		byte lengthArchive = data[0];
		byte versionArchive = data[1];
		if (lengthArchive == 8 && versionArchive == 3)
			return ArchiveV3.class;
		if (lengthArchive == 8 && versionArchive == 4)
			return ArchiveV4.class;
		if (lengthArchive == 4 && versionArchive == 1)
			return ArchiveV1.class;
		return null;
	}

	public static int convertDateToAddress(LocalDate date, Class<? extends IArchive> ac, ArchiveTypes type)
			throws Exception {
		int year = date.getYear();
		int month = date.getMonthValue();
		int day = date.getDayOfMonth();
		switch (type) {
		case MONTH:
			Field field = ac.getDeclaredField("monthArchive");
			long beginAddr = field.getAnnotation(Address.class).value();
			int recSize = field.getType().getAnnotation(RecordLength.class).value();
			if (ac.equals(ArchiveV3.class) || ac.equals(ArchiveV4.class)) {
				int Yt = year - 2015;
				while (Yt > 2)
					Yt -= 3; // приведем год к 0..2
				return (int) (beginAddr + (month - 1) * recSize + Yt * 12 * recSize);
			}
			if (ac.equals(ArchiveV1.class)) {
				return (int) (beginAddr + month * recSize - recSize);
			}
			break;
		case DAY:

			field = ac.getDeclaredField("dayArchive");
			beginAddr = field.getAnnotation(Address.class).value();
			recSize = field.getType().getAnnotation(RecordLength.class).value();

			if (ac.equals(ArchiveV3.class) || ac.equals(ArchiveV4.class)) {
				int Mt = month;
				if (Mt > 6)
					Mt -= 6; // приведем месяц к полгода
				return (int) (beginAddr + (day - 1) * recSize + (Mt - 1) * 31 * recSize);
			}
			if (ac.equals(ArchiveV1.class)) {
				if (month % 2 == 0) {
					return (int) (beginAddr + day * recSize - recSize + 31 * recSize);
				}
				return (int) (beginAddr + day * recSize - recSize);
			}
			break;
		case HOUR:
			field = ac.getDeclaredField("hourArchive");
			beginAddr = field.getAnnotation(Address.class).value();
			recSize = field.getType().getAnnotation(RecordLength.class).value();
			if (ac.equals(ArchiveV3.class) || ac.equals(ArchiveV4.class)) {
				long a = beginAddr + (day - 1) * (24 * recSize);
				if (month % 2 == 0)
					a += 31 * (24 * recSize);
				return (int) a;
			}
			if (ac.equals(ArchiveV1.class)) {
				long addr = beginAddr+(day-1)*24*recSize;
				if (month == 2 || month == 5 || month == 8 || month == 11) {
					if(day<17)
						addr+=31*24*recSize;
					else
						addr-=16*24*recSize;

				}
				if (month == 3 || month == 6 || month == 9 || month == 12) {
					addr+=15*24*recSize;
				}
				return (int) addr;
			}
			break;
		default:
			break;
		}
		return 0;

	}
}
