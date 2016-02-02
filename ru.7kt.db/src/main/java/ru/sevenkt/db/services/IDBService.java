package ru.sevenkt.db.services;

import java.time.LocalDate;
import java.util.List;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.entities.Parameter;

public interface IDBService {
	
	void saveNode(Node node);
	
	void saveArchiveType(ArchiveType archiveType);
	
	void saveDevice(Device device);
	
	void saveMeasuring(Measuring measuring);
	
	void saveParameter(Parameter parameter);
	
	void deleteNode(Node node);
	
	void deleteArchiveType(ArchiveType archiveType);
	
	void deleteDevice(Device device);
	
	void deleteMeasuring(Measuring measuring);
	
	void deleteParameter(Parameter parameter);
	
	List<Node> findAllNodes();
	
	List<Device> findDeviceByNode(Node node);
	
	List<Measuring> findMonthArchive(Device device, LocalDate start, LocalDate end);
	
	List<Measuring> findDayArchive(Device device, LocalDate start, LocalDate end);

	List<Measuring> findHourArchive(Device device, LocalDate start, LocalDate end);
	
	
	
}
