package ru.sevenkt.reader.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import gnu.io.NRSerialPort;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveFactory;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.reader.services.Archive7KTC;
import ru.sevenkt.reader.services.IReaderService;

public class ReaderImpl implements IReaderService {

	@Override
	public Set<String> getAvailableSerialPorts() {
		return NRSerialPort.getAvailableSerialPorts();
	}

	@Override
	public IArchive readFullArchive(Connection con) throws Exception {
		ReaderCable readerCable = new ReaderCable(con.getPort(), 2400);
		if (readerCable.connect()) {
			byte[] data = readerCable.readEEPROM(0, 256);
			readerCable.disconnect();
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

	@Override
	public List<Archive7KTC> readListArchiveFrom7KTC(String port, boolean withDeleted) throws Exception {
		Reader7KTC reader = new Reader7KTC(port);
		reader.connect();
		try {
			byte[] pic = reader.readPic();
			byte[] attributes = reader.readAttributes();
			Map<Integer, byte[]> map = reader.getArchivesList(true, attributes, pic);
			if (map == null)
				throw new Exception("Не возможно получить список архивов");
			List<Archive7KTC> archives = new ArrayList<>();
			for (Integer key : map.keySet()) {
				byte[] bytes = map.get(key);
				Archive7KTC archive7KTC;
				if (attributes[key / 64] == 1)
					archive7KTC = new Archive7KTC(key, 512, 1, 64);
				else
					archive7KTC = new Archive7KTC(key, 512, 2, 64);
				byte[] data = Arrays.copyOf(bytes,
						archive7KTC.getSizePage() * archive7KTC.getNumberOfBlock() * archive7KTC.getPagesInBlock());
				archive7KTC.setData(data);
				IArchive arch = ArchiveFactory.createArhive(archive7KTC.getData());
				archive7KTC.setDatetime(arch.getCurrentData().getCurrentDateTime());
				archive7KTC.setSerialNum(arch.getSettings().getSerialNumber() + "");
				archives.add(archive7KTC);
			}
			return archives;
		} catch (Exception e) {
			throw e;
		} finally {
			reader.disconnect();
		}
	}

	@Override
	public int getArchiveCount(String port, boolean whithDeleted) throws Exception {
		Reader7KTC reader = new Reader7KTC(port);
		reader.connect();
		int count = 0;
		try {
			byte[] pic = reader.readPic();
			byte[] attr = reader.readAttributes();
			if (pic == null || attr == null)
				throw new Exception("Не возможно считать данные. Проверьте правильность подсоединения устройства");
			for (byte b : attr) {
				if (b == 1 || b == 2)
					count++;
				if (!whithDeleted && count >= pic[0] && count > 0) {
					count--;
					break;
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			reader.disconnect();
		}
		return count;
	}

	@Override
	public Archive7KTC readFirstPageArchive(String port, int address) throws Exception {
		Reader7KTC reader = new Reader7KTC(port);
		reader.connect();
		try {
			byte[] attributes = reader.readAttributes();
			Archive7KTC archive7KTC;
			if (attributes[address / 64] == 1 || attributes[address / 64] == 2)
				archive7KTC = new Archive7KTC(address, 512, attributes[address / 64], 64);
			else {
				address += 64;
				archive7KTC = new Archive7KTC(address, 512, attributes[address / 64], 64);
			}
			byte[] bytes = reader.readPage(address);
			if (attributes == null || bytes == null)
				throw new Exception("Не возможно получить список архивов");

			byte[] data = Arrays.copyOf(bytes,
					archive7KTC.getSizePage() * archive7KTC.getNumberOfBlock() * archive7KTC.getPagesInBlock());
			archive7KTC.setData(data);
			IArchive arch = ArchiveFactory.createArhive(archive7KTC.getData());
			archive7KTC.setDatetime(arch.getCurrentData().getCurrentDateTime());
			archive7KTC.setSerialNum(arch.getSettings().getSerialNumber() + "");
			return archive7KTC;
		} catch (Exception e) {
			throw e;
		} finally {
			reader.disconnect();
		}
	}

	@Override
	public IArchive readArchive(Archive7KTC archive, String port) throws Exception {
		Reader7KTC reader = new Reader7KTC(port);
		reader.connect();
		try {
			int blockNum = archive.getFirstBlockNum();
			int endBlockNum = archive.getNumberOfBlock() * 64 + blockNum;
			byte[] bytes=Arrays.copyOfRange(archive.getData(), 0, archive.getSizePage());
			List<Byte> byteList = new ArrayList<>();
			byteList.addAll(Arrays.asList(ArrayUtils.toObject(bytes)));
			for (int i = blockNum + 1; i < endBlockNum; i++) {
				bytes = reader.readPage(i);
				if (bytes == null)
					throw new Exception("Не возможно получить архив " + archive);
				byteList.addAll(Arrays.asList(ArrayUtils.toObject(bytes)));
				
			}
			bytes=ArrayUtils.toPrimitive(byteList.toArray(new Byte[byteList.size()]));
			return ArchiveFactory.createArhive(bytes);
		} catch (Exception e) {
			throw e;
		} finally {
			reader.disconnect();
		}
	}

}
