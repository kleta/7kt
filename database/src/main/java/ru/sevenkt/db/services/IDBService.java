package ru.sevenkt.db.services;

import java.time.LocalDate;
import java.util.List;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

public interface IDBService {
	
	void saveNode(Node node);
	

	
	void saveDevice(Device device);
	
	void saveMeasuring(Measuring measuring);
	
	void deleteNode(Node node);

	void deleteDevice(Device device);
	
	void deleteMeasuring(Measuring measuring);
	
	List<Node> findAllNodes();
	
	List<Device> findDeviceByNode(Node node);
	
	Device findDeviceBySerialNum(int serialNumber);

	void saveMeasurings(List<Measuring> measurings);

	List<Device> findAllDevices();

	List<Measuring> findArchive(Device device, LocalDate startDate, LocalDate endDate, ArchiveTypes archiveType);

	List<Journal> findJournal(Device device, LocalDate startDate, LocalDate endDate);
	
	void saveJournal(List<Journal> list);
	
	
	
	
}
