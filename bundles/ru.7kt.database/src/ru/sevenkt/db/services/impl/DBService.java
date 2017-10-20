package ru.sevenkt.db.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.ErrorRepo;
import ru.sevenkt.db.repositories.JournalRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.ParamsRepo;
import ru.sevenkt.db.repositories.ReportRepo;
import ru.sevenkt.db.repositories.SchedulerGroupRepo;
import ru.sevenkt.db.services.ArchiveConverter;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.IJournalSettingsRecord;

@Service
public class DBService implements IDBService {

	@Autowired
	private DeviceRepo dr;

	@Autowired
	private ParamsRepo pr;

	@Autowired
	private MeasuringRepo measuringRepo;

	@Autowired
	private SchedulerGroupRepo sgr;

	@Autowired
	private JournalRepo jr;

	@Autowired
	private ErrorRepo er;

	@Autowired
	private EntityManager em;
	@Autowired
	EntityManagerFactory emf;

	@Autowired
	private ReportRepo rr;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	private void init() {

	}

	@Override
	public void saveSchedulerGroup(SchedulerGroup gr) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			List<ArchiveType> types = gr.getArchiveTypes();
			if(gr.getId()==null){
				gr.setArchiveTypes(null);
				sgr.save(gr);
				gr.setArchiveTypes(types);
			}
			sgr.save(gr);
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback();
			}
		}
	}

	@Override
	public void saveDevice(Device device) throws Exception {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			List<Params> params = device.getParams();
			if (device.getId() == null) {
				device.setParams(null);
				dr.save(device);
				device.setParams(params);
			}
			dr.save(device);
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	}

	@Override
	public void saveMeasuring(Measuring measuring) {
		LocalDateTime from = LocalDateTime.now();
		measuring.setTimestamp(LocalDateTime.now());
		em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			measuringRepo.save(measuring);
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback();
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveMeasuring() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public void deleteSchedulerGroup(SchedulerGroup node) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			sgr.delete(node);
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback();
			}
		}

	}

	@Override
	public void deleteDevice(Device device) {
		EntityTransaction tx = em.getTransaction();
		Measuring minM = measuringRepo.findTopByDeviceOrderByDateTimeAsc(device);
		Measuring maxM = measuringRepo.findTopByDeviceOrderByDateTimeDesc(device);

		if (minM != null && maxM != null) {
			LocalDateTime min = minM.getDateTime();
			LocalDateTime max = maxM.getDateTime();
			while (min.isBefore(max)) {
				tx.begin();
				try {
					measuringRepo.deleteByDeviceAndDateTimeBetween(device, min, min.plusMonths(1));
					er.deleteAll();
					min = min.plusMonths(1);
				} catch (Exception ex) {
					tx.rollback();
					ex.printStackTrace();
					throw ex;
				} finally {
					try {
						tx.commit();
					} catch (Exception e) {
						e.printStackTrace();
						tx.rollback();
					}
				}
			}
		}
		tx.begin();
		try {
			jr.deleteAll();
			long count = dr.count();
			if (count == 1)
				pr.deleteAll();
			dr.delete(device);
		} catch (Exception ex) {
			tx.rollback();
			ex.printStackTrace();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void deleteMeasuring(Measuring measuring) {

	}

	@Override
	public List<SchedulerGroup> findAllShedulerGroup() {
		return StreamSupport.stream(sgr.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<Device> findDeviceByNode(SchedulerGroup node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device findDeviceBySerialNum(int serialNumber) {
		return dr.findBySerialNum(serialNumber + "");
	}

	@Override
	public void saveMeasurings(List<Measuring> measurings) {
		LocalDateTime from = LocalDateTime.now();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			for (Measuring measuring : measurings) {
				measuring.setTimestamp(LocalDateTime.now());
				measuringRepo.save(measuring);
			}
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveMeasurings() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public List<Device> findAllDevices() {
		List<Device> devices = StreamSupport.stream(dr.findAll().spliterator(), false).collect(Collectors.toList());
		return devices;
	}

	@Override
	public List<Measuring> findArchive(Device device, LocalDateTime startDate, LocalDateTime endDate,
			ArchiveTypes archiveType) {
		List<Measuring> retVal = measuringRepo.findByDeviceAndArchiveTypeAndDateTimeBetween(device, archiveType, startDate, endDate);
		return retVal;
	}

	@Override
	public List<Journal> findJournal(Device device, LocalDate startDate, LocalDate endDate) {
		List<Journal> ret = jr.findByDeviceAndDateTimeBetween(device, startDate, endDate);
		return ret;
	}

	@Override
	public void saveJournal(List<Journal> journals) {
		LocalDateTime from = LocalDateTime.now();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			for (Journal journal : journals) {
				jr.save(journal);
			}
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback();
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveJournal() время " + ChronoUnit.MILLIS.between(from, to));

	}

	@Override
	public List<Journal> findJournal(Device device) {
		List<Journal> ret = jr.findByDevice(device);
		return ret;
	}

	@Override
	public void insertMonthArchive(ArchiveConverter archive) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		List<Error> errors = archive.getMonthErrors();
		saveErrors(errors);
		List<Measuring> measurings = archive.getMonthData();
		saveMeasurings(measurings);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertMonthArchive() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public void insertHourArchive(ArchiveConverter archive) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		List<Error> errors = archive.getHourErrors();
		saveErrors(errors);
		List<Measuring> measurings = archive.getHourData();
		saveMeasurings(measurings);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertHourArchive() время " + ChronoUnit.MILLIS.between(from, to));
	}

	private void saveErrors(List<Error> errors) {
		LocalDateTime from = LocalDateTime.now();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			er.save(errors);

		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			try {
				tx.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveErrors() время " + ChronoUnit.MILLIS.between(from, to));

	}

	@Override
	public void insertDayArchive(ArchiveConverter archive) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		List<Error> errors = archive.getDayErrors();
		saveErrors(errors);
		List<Measuring> measurings = archive.getDayData();
		saveMeasurings(measurings);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertDayArchive() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public void insertJournalSettings(IArchive archive, Device device) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		IJournalSettings js = archive.getJournalSettings();
		if (js != null) {
			List<IJournalSettingsRecord> records = js.getRecords();
			List<Journal> list = new ArrayList<>();
			for (IJournalSettingsRecord journalSettingsRecord : records) {
				Journal record = new Journal();
				record.setDevice(device);
				record.setDateTime(journalSettingsRecord.getDateTime());
				record.setWorkHour(journalSettingsRecord.getWorkHour());
				record.setEvent(journalSettingsRecord.getEvent());
				list.add(record);
			}
			saveJournal(list);
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertJournalSettings() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public List<Error> findErrors(Device device, LocalDateTime startDate, LocalDateTime endDate,
			ArchiveTypes archiveType) {
		return er.findByDeviceAndArchiveTypeAndDateTimeBetween(device, archiveType, startDate, endDate);
	}

	@Override
	public List<Report> getReports(Device device) {
		return rr.findByDevice(device);
	}

	@Override
	public Report findReport(Integer reportId) {
		return rr.findOne(reportId);
	}

	@Override
	public List<SchedulerGroup> findSchedulerGroup(Device device) {
		sgr.findByDevices(device);
		return null;
	}

}
