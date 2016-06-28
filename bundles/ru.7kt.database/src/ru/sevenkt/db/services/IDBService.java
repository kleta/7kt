package ru.sevenkt.db.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.domain.Archive;
import ru.sevenkt.domain.ArchiveTypes;

public interface IDBService {
	
	void saveNode(Node node);
	

	
	void saveDevice(Device device) throws Exception;
	
	void saveMeasuring(Measuring measuring);
	
	void deleteNode(Node node);

	void deleteDevice(Device device);
	
	void deleteMeasuring(Measuring measuring);
	
	List<Node> findAllNodes();
	
	List<Device> findDeviceByNode(Node node);
	
	Device findDeviceBySerialNum(int serialNumber);

	void saveMeasurings(List<Measuring> measurings);

	List<Device> findAllDevices();

	List<Measuring> findArchive(Device device, LocalDateTime startDate, LocalDateTime endDate, ArchiveTypes archiveType);

	List<Journal> findJournal(Device device, LocalDate startDate, LocalDate endDate);
	
	void saveJournal(List<Journal> list);

	List<Journal> findJournal(Device device);


	void insertMonthArchive(Archive archive, Device device) throws Exception;



	void insertHourArchive(Archive archive, Device device) throws Exception;



	void insertDayArchive(Archive archive, Device device) throws Exception;



	void insertJournalSettings(Archive archive, Device device) throws Exception;


	List<ru.sevenkt.db.entities.Error> findErrors(Device device, LocalDate startDate, LocalDate endDate, ArchiveTypes archiveType);


	List<Report> getReports(Device device);


	Report findReport(Integer reportId);
	
}
