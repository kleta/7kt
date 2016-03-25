package ru.sevenkt.db.services.impl;

import java.time.LocalDate;
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
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.NodeRepo;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Service
public class DBService implements IDBService {



	@Autowired
	private DeviceRepo dr;

	@Autowired
	private MeasuringRepo mr;

	@Autowired
	private NodeRepo nr;

	@Autowired
	private EntityManager em;
	
	@PostConstruct
	private void init(){
		
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
		tx.begin();
		try {
			mr.deleteByDevice(device);
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
		return dr.findBySerialNum(serialNumber+"");
	}



	@Override
	public void saveMeasurings(List<Measuring> measurings) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			for (Measuring measuring : measurings) {
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
		return mr.findByDeviceAndArchiveTypeAndDateTimeBetween(device, archiveType, startDate.atTime(0,0), endDate.atTime(0,0));
	}



	

}
