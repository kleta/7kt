package ru.sevenkt.db.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.JournalRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.NodeRepo;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

@Service
public class DBService implements IDBService {

	@Autowired
	private DeviceRepo dr;

	@Autowired
	private MeasuringRepo mr;

	@Autowired
	private NodeRepo nr;

	@Autowired
	private JournalRepo jr;

	@Autowired
	private EntityManager em;

	@PostConstruct
	private void init() {

	}

	@Override
	public void saveNode(Node node) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			nr.save(node);
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
	public void saveDevice(Device device) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			dr.save(device);
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
	public void saveMeasuring(Measuring measuring) {
		measuring.setTimestamp(LocalDateTime.now());
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			mr.save(measuring);
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
	public void deleteNode(Node node) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			nr.delete(node);
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
		Measuring minM = mr.findTopByDeviceOrderByDateTimeAsc(device);
		Measuring maxM = mr.findTopByDeviceOrderByDateTimeDesc(device);

		if (minM != null && maxM != null) {
			LocalDateTime min = minM.getDateTime();
			LocalDateTime max = maxM.getDateTime();
			while (min.isBefore(max)) {
				tx.begin();
				try {
					mr.deleteByDeviceAndDateTimeBetween(device, min, min.plusMonths(1));
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
				tx.rollback();
			}
		}

	}

	@Override
	public void deleteMeasuring(Measuring measuring) {

	}

	@Override
	public List<Node> findAllNodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Device> findDeviceByNode(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device findDeviceBySerialNum(int serialNumber) {
		return dr.findBySerialNum(serialNumber + "");
	}

	@Override
	public void saveMeasurings(List<Measuring> measurings) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			for (Measuring measuring : measurings) {
				measuring.setTimestamp(LocalDateTime.now());
				mr.save(measuring);
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

	}

	@Override
	public List<Device> findAllDevices() {
		return StreamSupport.stream(dr.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<Measuring> findArchive(Device device, LocalDate startDate, LocalDate endDate,
			ArchiveTypes archiveType) {
		return mr.findByDeviceAndArchiveTypeAndDateTimeBetween(device, archiveType, startDate.atTime(0, 0),
				endDate.atTime(0, 0));
	}

	@Override
	public List<Journal> findJournal(Device device, LocalDate startDate, LocalDate endDate) {
		List<Journal> ret = jr.findByDeviceAndDateTimeBetween(device, startDate, endDate);
		return ret;
	}

	@Override
	public void saveJournal(List<Journal> journals) {
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

	}

	@Override
	public List<Journal> findJournal(Device device) {
		List<Journal> ret = jr.findByDevice(device);
		return ret;
	}

	@Override
	public Double getSmoothedMultiplier(Measuring measuring) {
		if ((measuring.getParameter().getCategory().equals(ParametersConst.VOLUME)
				|| measuring.getParameter().getCategory().equals(ParametersConst.ENERGY))
				&& measuring.getArchiveType().equals(ArchiveTypes.HOUR)) {
			LocalDateTime dt = measuring.getDateTime();
			LocalDateTime dtFrom = null;
			LocalDateTime dtTo = null;
			if (dt.toLocalTime().equals(LocalTime.of(0, 0))) {
				dtFrom = dt.minusHours(23);
				dtTo = dt;
			} else {
				dtFrom = dt.withHour(1);
				dtTo = dtFrom.plusHours(23);
			}
			Double sum = mr.getSumHoursValue(measuring.getParameter(), measuring.getDevice(),ArchiveTypes.HOUR, dtFrom, dtTo);
			Measuring prevDay = mr.findByParameterAndDeviceAndArchiveTypeAndDateTime(measuring.getParameter(),
					measuring.getDevice(), ArchiveTypes.DAY, dtTo.minusDays(1));
			Measuring thisDay = mr.findByParameterAndDeviceAndArchiveTypeAndDateTime(measuring.getParameter(),
					measuring.getDevice(), ArchiveTypes.DAY, dtTo);
			if (prevDay != null && thisDay != null && sum != 0)
				return (thisDay.getValue() - prevDay.getValue()) / sum;

		}
		return (double) 1;
	}

}
