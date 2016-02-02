package ru.sevenkt.db.services.impl;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.entities.Parameter;
import ru.sevenkt.db.repositories.ArchiveTypeRepo;
import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.NodeRepo;
import ru.sevenkt.db.repositories.ParameterRepo;
import ru.sevenkt.db.services.IDBService;

@Service
public class DBService implements IDBService {

	@Autowired
	private ParameterRepo pr;

	@Autowired
	private ArchiveTypeRepo atr;

	@Autowired
	private DeviceRepo dr;

	@Autowired
	private MeasuringRepo mr;

	@Autowired
	private NodeRepo nr;

	@Autowired
	private JpaTransactionManager jtm;

	
	// EntityManagerFactory emf;
	@Autowired
	private EntityManager em;



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
	public void saveArchiveType(ArchiveType archiveType) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			atr.save(archiveType);
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
	public void saveParameter(Parameter parameter) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			pr.save(parameter);
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
	public void deleteArchiveType(ArchiveType archiveType) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			atr.delete(archiveType);
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
			dr.delete(device);
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
	public void deleteMeasuring(Measuring measuring) {
		
		
	}


	@Override
	public void deleteParameter(Parameter parameter) {
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try {
			pr.delete(parameter);
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
	public List<Measuring> findMonthArchive(Device device, LocalDate start, LocalDate end) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Measuring> findDayArchive(Device device, LocalDate start, LocalDate end) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Measuring> findHourArchive(Device device, LocalDate start, LocalDate end) {
		// TODO Auto-generated method stub
		return null;
	}

}
