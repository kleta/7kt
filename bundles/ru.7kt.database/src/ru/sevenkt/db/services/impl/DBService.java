package ru.sevenkt.db.services.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Node;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.ErrorRepo;
import ru.sevenkt.db.repositories.JournalRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.NodeRepo;
import ru.sevenkt.db.repositories.ParamsRepo;
import ru.sevenkt.db.repositories.ReportRepo;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.Archive;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.DayRecord;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.HourRecord;
import ru.sevenkt.domain.JournalSettings;
import ru.sevenkt.domain.JournalSettingsRecord;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.MonthRecord;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;
import ru.sevenkt.domain.Settings;

@Service
public class DBService implements IDBService {

	@Autowired
	private DeviceRepo dr;

	@Autowired
	private ParamsRepo pr;

	@Autowired
	private MeasuringRepo measuringRepo;

	@Autowired
	private NodeRepo nr;

	@Autowired
	private JournalRepo jr;

	@Autowired
	private ErrorRepo er;

	@Autowired
	private EntityManager em;

	@Autowired
	private ReportRepo rr;

	private Logger LOG = LoggerFactory.getLogger(getClass());

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
				tx.rollback();
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveMeasurings() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public List<Device> findAllDevices() {
		return StreamSupport.stream(dr.findAll().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<Measuring> findArchive(Device device, LocalDateTime startDate, LocalDateTime endDate,
			ArchiveTypes archiveType) {
		return measuringRepo.findByDeviceAndArchiveTypeAndDateTimeBetween(device, archiveType, startDate, endDate);
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
	public void insertMonthArchive(Archive archive, Device device) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		MonthArchive ma = archive.getMonthArchive();
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime().withDayOfMonth(1);
		LocalDate startArchiveDate = dateTime.minusYears(MonthArchive.MAX_YEAR_COUNT).toLocalDate();
		List<Measuring> measurings = new ArrayList<>();
		while (!startArchiveDate.isAfter(dateTime.toLocalDate())) {
			int year = startArchiveDate.getYear() - 2000;
			if (year < 15) {
				startArchiveDate = startArchiveDate.plusMonths(1);
				continue;
			}
			MonthRecord mr = ma.getMonthRecord(year, startArchiveDate.getMonthValue());
			if (mr.isValid()) {
				Field[] fields = MonthRecord.class.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Parameter.class)) {
						Measuring m = new Measuring();
						m.setArchiveType(ArchiveTypes.MONTH);
						m.setDateTime(mr.getDate().atTime(0, 0));
						m.setDevice(device);
						m.setParameter(field.getAnnotation(Parameter.class).value());
						Parameters parameter = field.getAnnotation(Parameter.class).value();
						switch (parameter) {
						case AVG_TEMP1:
						case AVG_TEMP2:
						case AVG_TEMP3:
						case AVG_TEMP4: {
							float val = field.getInt(mr);
							m.setValue((double) (val / 100 > 150 ? 0 : val / 100));
							measurings.add(m);
						}
							break;
						case AVG_P1:
						case AVG_P2: {
							float val = field.getFloat(mr);
							val = val / 10;
							m.setValue(new Double(val + ""));
							measurings.add(m);
						}
							break;
						case ERROR_BYTE1:
						case ERROR_BYTE2:
							m.setValue((double) field.getInt(mr));
							measurings.add(m);
							break;
						case ERROR_TIME1:
						case ERROR_TIME2: {
							// if
							// (m.getDateTime().isAfter(LocalDateTime.of(2015,
							// 12, 17, 0, 0)))
							// System.out.println();
							LocalDateTime dtTo = m.getDateTime();
							LocalDateTime dtFrom = dtTo.minusMonths(1);
							long hours = ChronoUnit.HOURS.between(dtFrom, dtTo);
							m.setValue((double) field.getInt(mr));
							break;
						}

						default:
							float val = field.getFloat(mr);
							m.setValue(new Double(val + ""));
							measurings.add(m);
							break;
						}

//						if (!(startArchiveDate.isAfter(dateTime.minusDays(HourArchive.MAX_DAY_COUNT).toLocalDate())
//								&& (parameter.equals(Parameters.ERROR_TIME1)
//										|| parameter.equals(Parameters.ERROR_TIME2))))
//							
					}
				}
				if (startArchiveDate.isBefore(dateTime.minusDays(HourArchive.MAX_DAY_COUNT).toLocalDate()))
					saveMonthErrors(mr, device);
			}
			startArchiveDate = startArchiveDate.plusMonths(1);
		}
		saveMeasurings(measurings);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertMonthArchive() время " + ChronoUnit.MILLIS.between(from, to));

	}

	private void saveMonthErrors(MonthRecord mr, Device device) {
		LocalDateTime from = LocalDateTime.now();
		int e1 = mr.getErrorChannel1();
		int e2 = mr.getErrorChannel2();
		LocalDate date = mr.getDate();
		// if(date.isAfter(LocalDate.of(2015,10,1)))
		// System.out.println();
		List<Error> errors = parseErrors(e1, e2, ArchiveTypes.MONTH, date, device);
		if (!errors.isEmpty())
			saveErrors(errors);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveMonthErrors() время " + ChronoUnit.MILLIS.between(from, to));
	}

	@Override
	public void insertHourArchive(Archive archive, Device device) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		HourArchive ha = archive.getHourArchive();
		DayArchive da = archive.getDayArchive();
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime();
		LocalDateTime startArchiveDate = dateTime.minusDays(HourArchive.MAX_DAY_COUNT).withHour(1);
		List<Error> errors = er.findByDeviceAndArchiveTypeAndDateTimeBetween(device, ArchiveTypes.HOUR,
				startArchiveDate, dateTime);
		er.delete(errors);
		errors.clear();
		while (!startArchiveDate.isAfter(dateTime)) {
			List<Measuring> measurings = new ArrayList<>();
			LocalDate localDate = startArchiveDate.toLocalDate();
			DayRecord sumDay = ha.getDayConsumption(localDate, dateTime, archive.getSettings());
			DayRecord dr2 = da.getDayRecord(localDate.plusDays(1), dateTime);
			DayRecord dr1 = da.getDayRecord(localDate, dateTime);
			DayRecord dayConsumption;
			if (!dr2.isValid() || !dr1.isValid())
				dayConsumption = sumDay;
			else
				dayConsumption = dr2.minus(dr1);
			for (int i = 1; i < 25; i++) {
				LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0)).plusHours(i);
				HourRecord hr = ha.getHourRecord(localDateTime, dateTime);
				// System.out.println(localDateTime);
				if (hr.isValid()) {
					List<Measuring> hourMeasurings = geHourtMeasurings(hr, localDateTime, archive.getSettings());
					measurings.addAll(hourMeasurings);
					// List<Error> hourErrors = calculateErrors(hr,
					// archive.getSettings(), device);
					// errors.addAll(hourErrors);
					Measuring m = new Measuring();
					m.setArchiveType(ArchiveTypes.HOUR);
					m.setDateTime(localDateTime);

					m.setTimestamp(LocalDateTime.now());
					m.setValue(new Double("0"));
					m.setParameter(Parameters.ERROR_TIME1);
					measurings.add(m);

					m = new Measuring();
					m.setArchiveType(ArchiveTypes.HOUR);
					m.setDateTime(localDateTime);
					// m.setDevice(e1v1t1Errors.get(0).getDevice());
					m.setTimestamp(LocalDateTime.now());
					m.setValue(new Double("0"));
					m.setParameter(Parameters.ERROR_TIME2);
					measurings.add(m);
				}
			}
			if (!measurings.isEmpty()) {
				smoothHourMeasurings(measurings, dayConsumption, sumDay);
				measurings.forEach(m -> m.setDevice(device));
				List<Error> hourErrors = calculateHourErrors(measurings);
				errors.addAll(hourErrors);
				saveMeasurings(measurings);
			}
			startArchiveDate = startArchiveDate.plusDays(1);
		}
		errors.forEach(e -> e.setDevice(device));
		saveHourErrors(errors);
		insertHourErrorTimes(errors);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertHourArchive() время " + ChronoUnit.MILLIS.between(from, to));
	}

	private List<Error> calculateHourErrors(List<Measuring> measurings) {
		LocalDateTime from = LocalDateTime.now();
		List<Error> errors = new ArrayList<>();
		Device device = measurings.get(0).getDevice();
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		Set<LocalDateTime> keySet = groupByDateTime.keySet();
		String formula = device.getFormulaNum() + "";
		for (LocalDateTime dt : keySet) {
			List<Measuring> lm = groupByDateTime.get(dt);
			double t1 = lm.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP1))
					.mapToDouble(Measuring::getValue).findFirst().getAsDouble();
			double t2 = lm.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP2))
					.mapToDouble(Measuring::getValue).findFirst().getAsDouble();
			double t3 = lm.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP3))
					.mapToDouble(Measuring::getValue).findFirst().getAsDouble();
			double t4 = lm.stream().filter(m -> m.getParameter().equals(Parameters.AVG_TEMP4))
					.mapToDouble(Measuring::getValue).findFirst().getAsDouble();

			double v1 = lm.stream().filter(m -> m.getParameter().equals(Parameters.V1)).mapToDouble(Measuring::getValue)
					.findFirst().getAsDouble();
			double v2 = lm.stream().filter(m -> m.getParameter().equals(Parameters.V2)).mapToDouble(Measuring::getValue)
					.findFirst().getAsDouble();
			double v3 = lm.stream().filter(m -> m.getParameter().equals(Parameters.V3)).mapToDouble(Measuring::getValue)
					.findFirst().getAsDouble();
			double v4 = lm.stream().filter(m -> m.getParameter().equals(Parameters.V4)).mapToDouble(Measuring::getValue)
					.findFirst().getAsDouble();
			Double errorByte1 = lm.stream().filter(m -> m.getParameter().equals(Parameters.ERROR_BYTE1))
					.mapToDouble(Measuring::getValue).findFirst().getAsDouble();
			if (formula.length() > 1) {
				switch (formula.charAt(1) + "") {
				case "1":
					if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 == 0
							|| v2 < device.getwMin1() || v2 > device.getwMax34()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 * 1.2 < v2 || t1 < t2) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "2":
					if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (t1 < t2) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "3":
					if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v2 == 0 || v2 < device.getwMin1() || v2 > device.getwMax34()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (t1 < t2) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "5":
					if (t1 > 150 || t1 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 * 1.2 < v2) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "6":
					if (t1 > 150 || t1 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				}
				switch (formula.charAt(0) + "") {
				case "1":
					if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12() || v4 == 0
							|| v4 < device.getwMin1() || v4 > device.getwMax34()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v3 * 1.2 < v4 || t3 < t4) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "2":
					if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (t3 < t4) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "3":
					if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v4 == 0 || v4 < device.getwMin0() || v4 > device.getwMax12()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (t3 < t4) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "5":
					if (t3 > 150 || t3 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v3 * 1.2 < v4) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.E2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				case "6":
					if (t3 > 150 || t3 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T2);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					break;
				}
			} else {
				if (formula.equals("41")) {
					if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60 || t3 > 150 || t3 < -60) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.T1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
					if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 == 0
							|| v2 < device.getwMin1() || v2 > device.getwMax34()) {
						Error error = new Error();
						error.setArchiveType(ArchiveTypes.HOUR);
						error.setDateTime(dt);
						error.setErrorCode(ErrorCodes.V1);
						error.setTimestamp(LocalDateTime.now());
						errors.add(error);
					}
				} else {
					switch (formula.charAt(0) + "") {
					case "1":
						if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.T1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 == 0
								|| v2 < device.getwMin1() || v2 > device.getwMax34()) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.V1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v1 * 1.2 < v2 || t1 < t2) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.E1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						break;
					case "2":
						if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.T1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.V1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (t1 < t2) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.E1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						break;
					case "3":
						if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.T1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v2 == 0 || v2 < device.getwMin1() || v2 > device.getwMax34()) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.V1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (t1 < t2) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.E1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						break;
					case "5":
						if (t1 > 150 || t1 < -60) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.T1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.V1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						if (v1 * 1.2 < v2) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.E1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						break;
					case "6":
						if (t1 > 150 || t1 < -60) {
							Error error = new Error();
							error.setArchiveType(ArchiveTypes.HOUR);
							error.setDateTime(dt);
							error.setErrorCode(ErrorCodes.T1);
							error.setTimestamp(LocalDateTime.now());
							errors.add(error);
						}
						break;
					}
				}
			}
			if (device.isControlPower()) {
				int a = errorByte1.intValue() & 0b10000000;
				if (a == 0b10000000) {
					Error error = new Error();
					error.setArchiveType(ArchiveTypes.HOUR);
					error.setDateTime(dt);
					error.setErrorCode(ErrorCodes.U);
					error.setTimestamp(LocalDateTime.now());
					errors.add(error);
				}
			}

		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод calculateHourErrors() время " + ChronoUnit.MILLIS.between(from, to));
		return errors;
	}

	private void insertHourErrorTimes(List<Error> errors) {
		LocalDateTime from = LocalDateTime.now();
		errors = errors.stream().filter(e->e.getArchiveType().equals(ArchiveTypes.DAY)).collect(Collectors.toList());
		if (!errors.isEmpty()) {
			Device device = errors.get(0).getDevice();
			Map<LocalDateTime, List<Error>> groupByDateTime = errors.stream()
					.collect(Collectors.groupingBy(Error::getDateTime));
			Set<LocalDateTime> keySet = groupByDateTime.keySet();
			List<LocalDateTime> keyList = keySet.stream().sorted((d1, d2) -> d1.compareTo(d2))
					.collect(Collectors.toList());
			Map<LocalDateTime, Integer> dayErrorHours1 = new HashMap<>();
			Map<LocalDateTime, Integer> monthErrorHours1 = new HashMap<>();
			Map<LocalDateTime, Integer> dayErrorHours2 = new HashMap<>();
			Map<LocalDateTime, Integer> monthErrorHours2 = new HashMap<>();
			for (LocalDateTime localDateTime : keyList) {
				List<Error> errorsWithoutU = groupByDateTime.get(localDateTime).stream()
						.filter(e -> !e.getErrorCode().equals(ErrorCodes.U)).collect(Collectors.toList());
				List<Error> e1v1t1Errors = errorsWithoutU
						.stream().filter(e -> e.getErrorCode().equals(ErrorCodes.E1)
								|| e.getErrorCode().equals(ErrorCodes.T1) || e.getErrorCode().equals(ErrorCodes.V1))
						.collect(Collectors.toList());
				List<Error> e2v2t2Errors = errorsWithoutU
						.stream().filter(e -> e.getErrorCode().equals(ErrorCodes.E2)
								|| e.getErrorCode().equals(ErrorCodes.T2) || e.getErrorCode().equals(ErrorCodes.V2))
						.collect(Collectors.toList());
				LocalTime time = localDateTime.toLocalTime();
				LocalDateTime dayDt, monthDt;
				if (time.equals(LocalTime.of(0, 0))) {
					dayDt = localDateTime.withHour(0).withMinute(0);
				} else
					dayDt = localDateTime.plusDays(1).withHour(0).withMinute(0);
				if (localDateTime.getDayOfMonth() == 1 && time.equals(LocalTime.of(0, 0)))
					monthDt = localDateTime.withHour(0).withMinute(0);
				else
					monthDt = localDateTime.withDayOfMonth(1).plusMonths(1).withHour(0).withMinute(0);
				if (!e1v1t1Errors.isEmpty()) {
					Measuring m = new Measuring();
					m.setArchiveType(ArchiveTypes.HOUR);
					m.setDateTime(localDateTime);
					m.setDevice(e1v1t1Errors.get(0).getDevice());
					m.setTimestamp(LocalDateTime.now());
					// m.setValue(new Double("0"));
					// // ErrorCodes errorCode = error.getErrorCode();

					m.setParameter(Parameters.ERROR_TIME1);
					m.setValue((double) 1);
					if (m.getDateTime().isAfter(LocalDateTime.of(2016, 5, 21, 0, 0)))
						System.out.println();
					Integer countHours = dayErrorHours1.get(dayDt);
					if (countHours == null) {
						countHours = 0;
						dayErrorHours1.put(dayDt, countHours);
					}
					dayErrorHours1.put(dayDt, ++countHours);
					countHours = monthErrorHours1.get(monthDt);
					if (countHours == null) {
						countHours = 0;
						monthErrorHours1.put(monthDt, countHours);
					}
					monthErrorHours1.put(monthDt, ++countHours);
					measuringRepo.save(m);
				}
				if (!e2v2t2Errors.isEmpty()) {
					Measuring m = new Measuring();
					m.setArchiveType(ArchiveTypes.HOUR);
					m.setDateTime(localDateTime);
					m.setDevice(e2v2t2Errors.get(0).getDevice());
					m.setTimestamp(LocalDateTime.now());
					m.setValue(new Double("0"));
					// ErrorCodes errorCode = error.getErrorCode();
					m.setParameter(Parameters.ERROR_TIME2);
					m.setValue((double) 1);
					Integer countHours = dayErrorHours2.get(dayDt);
					if (countHours == null) {
						countHours = 0;
						dayErrorHours2.put(dayDt, countHours);
					}
					dayErrorHours2.put(dayDt, ++countHours);
					countHours = monthErrorHours2.get(monthDt);
					if (countHours == null) {
						countHours = 0;
						monthErrorHours2.put(monthDt, countHours);
					}
					monthErrorHours2.put(monthDt, ++countHours);
					measuringRepo.save(m);
				}

			}

			Set<LocalDateTime> dayKeySet1 = dayErrorHours1.keySet();
			for (LocalDateTime dt : dayKeySet1)

			{
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.DAY);
				m.setDateTime(dt);
				m.setDevice(device);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(dayErrorHours1.get(dt) + 0.0);
				m.setParameter(Parameters.ERROR_TIME1);
				measuringRepo.save(m);
			}

			Set<LocalDateTime> dayKey2 = dayErrorHours2.keySet();
			for (LocalDateTime dt : dayKey2)

			{
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.DAY);
				m.setDateTime(dt);
				m.setDevice(device);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(dayErrorHours2.get(dt) + 0.0);
				m.setParameter(Parameters.ERROR_TIME2);
				measuringRepo.save(m);
			}

			Set<LocalDateTime> monthKeySet1 = monthErrorHours1.keySet();
			for (LocalDateTime dt : monthKeySet1) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.MONTH);
				m.setDateTime(dt);
				m.setDevice(device);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(monthErrorHours1.get(dt) + 0.0);
				m.setParameter(Parameters.ERROR_TIME1);
				measuringRepo.save(m);
			}

			Set<LocalDateTime> monthKey2 = monthErrorHours2.keySet();
			for (LocalDateTime dt : monthKey2) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.MONTH);
				m.setDateTime(dt);
				m.setDevice(device);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(monthErrorHours2.get(dt) + 0.0);
				m.setParameter(Parameters.ERROR_TIME2);
				measuringRepo.save(m);
			}

		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertHourErrorTimes() время " + ChronoUnit.MILLIS.between(from, to));

	}

	private void saveHourErrors(List<Error> errors) {
		LocalDateTime from = LocalDateTime.now();
		// saveErrors(errors);
		Map<ErrorCodes, List<Error>> groupByErrorCode = errors.stream()
				.collect(Collectors.groupingBy(Error::getErrorCode));
		Set<ErrorCodes> keySet = groupByErrorCode.keySet();
		Map<LocalDateTime, List<Error>> dayErrorsMap = new HashMap<>();
		Map<LocalDateTime, List<Error>> monthErrorsMap = new HashMap<>();
		for (ErrorCodes errorCode : keySet) {
			List<Error> ers = groupByErrorCode.get(errorCode);
			for (Error error : ers) {
				LocalTime time = error.getDateTime().toLocalTime();
				LocalDateTime dt;
				if (time.equals(LocalTime.of(0, 0))) {
					dt = error.getDateTime().withHour(0).withMinute(0);
				} else
					dt = error.getDateTime().plusDays(1).withHour(0).withMinute(0);
				Error e = new Error();
				e.setDateTime(dt);
				e.setArchiveType(ArchiveTypes.DAY);
				e.setErrorCode(errorCode);
				e.setTimestamp(LocalDateTime.now());
				e.setDevice(error.getDevice());
				List<Error> list = dayErrorsMap.get(dt);
				if (list == null) {
					list = new ArrayList<>();
					dayErrorsMap.put(dt, list);
				}
				if (!list.contains(e))
					list.add(e);
				if (dt.getDayOfMonth() == 1 && time.equals(LocalTime.of(0, 0)))
					dt = error.getDateTime().withHour(0).withMinute(0);
				else
					dt = error.getDateTime().withDayOfMonth(1).plusMonths(1).withHour(0).withMinute(0);
				e = new Error();
				e.setDateTime(dt);
				e.setArchiveType(ArchiveTypes.MONTH);
				e.setErrorCode(errorCode);
				e.setTimestamp(LocalDateTime.now());
				e.setDevice(error.getDevice());
				list = monthErrorsMap.get(dt);
				if (list == null) {
					list = new ArrayList<>();
					monthErrorsMap.put(dt, list);
				}
				if (!list.contains(e))
					list.add(e);
			}
		}
		Set<LocalDateTime> dayKeySet = dayErrorsMap.keySet();
		for (LocalDateTime localDateTime : dayKeySet) {
			errors.addAll(dayErrorsMap.get(localDateTime));
		}
		Set<LocalDateTime> monthKeySet = monthErrorsMap.keySet();
		for (LocalDateTime localDateTime : monthKeySet) {
			errors.addAll(monthErrorsMap.get(localDateTime));
		}
		saveErrors(errors);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод saveHourErrors() время " + ChronoUnit.MILLIS.between(from, to));
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

	// private List<Error> calculateErrors(HourRecord hr, Settings settings,
	// Device device) {
	// List<Error> errors = new ArrayList<>();
	// String formula = settings.getFormulaNum() + "";
	// int t1 = hr.getAvgTemp1() / 100;
	// int t2 = hr.getAvgTemp2() / 100;
	// int t3 = hr.getAvgTemp3() / 100;
	// int t4 = hr.getAvgTemp4() / 100;
	// int v1 = hr.getVolume1();
	// int v2 = hr.getVolume2();
	// int v3 = hr.getVolume3();
	// int v4 = hr.getVolume4();
	// LocalDateTime dateTime = hr.getDateTime();
	// if (formula.length() > 1) {
	// switch (formula.charAt(1) + "") {
	// case "1":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 ==
	// 0 || v2 < device.getwMin1()
	// || v2 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 * 1.2 < v2 || t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "2":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "3":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v2 == 0 || v2 < device.getwMin1() || v2 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "5":
	// if (t1 > 150 || t1 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 * 1.2 < v2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "6":
	// if (t1 > 150 || t1 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// }
	// switch (formula.charAt(0) + "") {
	// case "1":
	// if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12() || v4 ==
	// 0 || v4 < device.getwMin1()
	// || v4 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v3 * 1.2 < v4 || t3 < t4) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "2":
	// if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t3 < t4) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "3":
	// if (t3 > 150 || t3 < -60 || t4 > 150 || t4 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v4 == 0 || v4 < device.getwMin0() || v4 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t3 < t4) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "5":
	// if (t3 > 150 || t3 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v3 == 0 || v3 < device.getwMin0() || v3 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v3 * 1.2 < v4) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "6":
	// if (t3 > 150 || t3 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T2);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// }
	// } else {
	// if (formula.equals("41")) {
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60 || t3 > 150 || t3 < -60)
	// {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 ==
	// 0 || v2 < device.getwMin1()
	// || v2 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// } else {
	// switch (formula.charAt(0) + "") {
	// case "1":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12() || v2 ==
	// 0
	// || v2 < device.getwMin1() || v2 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 * 1.2 < v2 || t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "2":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "3":
	// if (t1 > 150 || t1 < -60 || t2 > 150 || t2 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v2 == 0 || v2 < device.getwMin1() || v2 > device.getwMax34()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (t1 < t2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "5":
	// if (t1 > 150 || t1 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 == 0 || v1 < device.getwMin0() || v1 > device.getwMax12()) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.V1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// if (v1 * 1.2 < v2) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.E1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// case "6":
	// if (t1 > 150 || t1 < -60) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.T1);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// break;
	// }
	// }
	// }
	// if (device.isControlPower()) {
	// int a = hr.getErrorChannel1() & 0b10000000;
	// if (a == 0b10000000) {
	// Error error = new Error();
	// error.setArchiveType(ArchiveTypes.HOUR);
	// error.setDateTime(dateTime);
	// error.setErrorCode(ErrorCodes.U);
	// error.setTimestamp(LocalDateTime.now());
	// errors.add(error);
	// }
	// }
	// return errors;
	// }

	@Override
	public void insertDayArchive(Archive archive, Device device) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		DayArchive da = archive.getDayArchive();
		List<Error> errors = new ArrayList<>();
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime().withHour(0);
		LocalDate startArchiveDate = dateTime.minusMonths(DayArchive.MAX_MONTH_COUNT).toLocalDate();
		List<Measuring> measurings = new ArrayList<>();
		while (!startArchiveDate.isAfter(dateTime.toLocalDate())) {
			DayRecord dr = da.getDayRecord(startArchiveDate, dateTime);
			if (dr.isValid()) {
				Field[] fields = DayRecord.class.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Parameter.class)) {
						Measuring m = new Measuring();
						m.setArchiveType(ArchiveTypes.DAY);
						m.setDevice(device);
						m.setDateTime(dr.getDate().atTime(0, 0));
						Parameters parameter = field.getAnnotation(Parameter.class).value();
						m.setParameter(parameter);
						switch (parameter) {
						case AVG_TEMP1:
						case AVG_TEMP2:
						case AVG_TEMP3:
						case AVG_TEMP4: {
							float val = field.getInt(dr);
							m.setValue((double) (val / 100 > 150 ? 0 : val / 100));
							measurings.add(m);
						}
							break;
						case AVG_P1:
						case AVG_P2: {
							float val = field.getFloat(dr);
							val = val / 10;
							m.setValue(new Double(val + ""));
							measurings.add(m);
						}
							break;
						case ERROR_BYTE1:
						case ERROR_BYTE2:
							int val1 = field.getInt(dr);
							// if(val1!=129 && val1!=1)
							// System.out.println();
							m.setValue((double) field.getInt(dr));
							measurings.add(m);
							break;
						case ERROR_TIME1:
						case ERROR_TIME2: {
							int val2 = field.getInt(dr);
							if (val2 != 24)
								System.out.println();
							m.setValue((double) field.getInt(dr));
							break;
						}
						default:
							float val = field.getFloat(dr);
							BigDecimal bdVal = new BigDecimal(val + "").setScale(10, BigDecimal.ROUND_HALF_UP);
							m.setValue(new Double(val + ""));
							measurings.add(m);
							break;
						}
						// if
						// (!(startArchiveDate.isAfter(dateTime.minusDays(HourArchive.MAX_DAY_COUNT).toLocalDate())
						// && (parameter.equals(Parameters.ERROR_TIME1)
						// || parameter.equals(Parameters.ERROR_TIME2))))
						
					}
				}
				List<Error> dayErrors;
				if (startArchiveDate.isBefore(dateTime.minusDays(HourArchive.MAX_DAY_COUNT).toLocalDate())) {
					dayErrors = getDayErrors(dr, device);
					errors.addAll(dayErrors);
				}
				// else
				// System.out.println(startArchiveDate);
			}

			startArchiveDate = startArchiveDate.plusDays(1);
		}
		saveErrors(errors);
		saveMeasurings(measurings);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertDayArchive() время " + ChronoUnit.MILLIS.between(from, to));
	}

	private List<Error> getDayErrors(DayRecord dr, Device device) {
		LocalDateTime from = LocalDateTime.now();
		int e1 = dr.getErrorChannel1();
		int e2 = dr.getErrorChannel2();
		LocalDate date = dr.getDate();
		// if (date.isAfter(LocalDate.of(2016, 1, 16)))
		// System.out.println();
		List<Error> errors = parseErrors(e1, e2, ArchiveTypes.DAY, date, device);
		// if (!errors.isEmpty())

		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод getDayErrors() время " + ChronoUnit.MILLIS.between(from, to));
		return errors;
	}

	private List<Error> parseErrors(int e1, int e2, ArchiveTypes aType, LocalDate localDate, Device device) {
		LocalDateTime from = LocalDateTime.now();
		List<Error> errors = new ArrayList<>();
		if (e1 == 0 && e2 == 0)
			return errors;
		if ((e1 & 3) != 0) {
			Error error = new Error();
			error.setArchiveType(aType);
			error.setDateTime(localDate.atStartOfDay());
			error.setDevice(device);
			error.setErrorCode(ErrorCodes.T1);
			error.setTimestamp(LocalDateTime.now());
			errors.add(error);
		}
		if ((e2 & 3) != 0) {
			Error error = new Error();
			error.setArchiveType(aType);
			error.setDateTime(localDate.atStartOfDay());
			error.setDevice(device);
			error.setErrorCode(ErrorCodes.T2);
			error.setTimestamp(LocalDateTime.now());
			errors.add(error);
		}
		if ((e1 & 12) != 0) {
			Error error = new Error();
			error.setArchiveType(aType);
			error.setDateTime(localDate.atStartOfDay());
			error.setDevice(device);
			error.setErrorCode(ErrorCodes.V1);
			error.setTimestamp(LocalDateTime.now());
			errors.add(error);
		}
		if ((e2 & 12) != 0) {
			Error error = new Error();
			error.setArchiveType(aType);
			error.setDateTime(localDate.atStartOfDay());
			error.setDevice(device);
			error.setErrorCode(ErrorCodes.V2);
			error.setTimestamp(LocalDateTime.now());
			errors.add(error);
		}
		if ((e1 & 128) != 0) {
			Error error = new Error();
			error.setArchiveType(aType);
			error.setDateTime(localDate.atStartOfDay());
			error.setDevice(device);
			error.setErrorCode(ErrorCodes.U);
			error.setTimestamp(LocalDateTime.now());
			errors.add(error);
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод parseErrors() время " + ChronoUnit.NANOS.between(from, to));
		return errors;
	}

	@Override
	public void insertJournalSettings(Archive archive, Device device) throws Exception {
		LocalDateTime from = LocalDateTime.now();
		JournalSettings js = archive.getJournalSettings();
		List<JournalSettingsRecord> records = js.getRecords();
		List<Journal> list = new ArrayList<>();
		for (JournalSettingsRecord journalSettingsRecord : records) {
			Journal record = new Journal();
			record.setDevice(device);
			record.setDateTime(journalSettingsRecord.getDateTime());
			record.setWorkHour(journalSettingsRecord.getWorkHour());
			record.setEvent(journalSettingsRecord.getEvent());
			list.add(record);
		}
		saveJournal(list);
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод insertJournalSettings() время " + ChronoUnit.MILLIS.between(from, to));
	}

	private List<Measuring> geHourtMeasurings(HourRecord hr, LocalDateTime localDateTime, Settings settings)
			throws IllegalArgumentException, IllegalAccessException {
		LocalDateTime from = LocalDateTime.now();
		List<Measuring> measurings = new ArrayList<>();
		Field[] fields = HourRecord.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			// if (field.getName().equals("errorChannel1") ||
			// field.getName().equals("errorChannel2"))
			// System.out.println(hr.getDateTime() + ":" + field.getName() + ":"
			// + Integer.toBinaryString(field.getInt(hr)) + ":" +
			// field.getInt(hr));
			if (field.isAnnotationPresent(Parameter.class)) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.HOUR);
				m.setDateTime(hr.getDateTime());
				// m.setDevice(device);
				Parameters parameter = field.getAnnotation(Parameter.class).value();
				m.setParameter(parameter);
				if (parameter.equals(Parameters.AVG_P1) || parameter.equals(Parameters.AVG_P2)) {
					float val = field.getInt(hr);
					m.setValue((double) (val / 10));
				}
				if (parameter.equals(Parameters.AVG_TEMP1) || parameter.equals(Parameters.AVG_TEMP2)
						|| parameter.equals(Parameters.AVG_TEMP3) || parameter.equals(Parameters.AVG_TEMP4)) {
					float val = field.getInt(hr);
					m.setValue((double) ((val / 100 > 150 || val / 100 < -60) ? 0 : val / 100));
				}

				if (parameter.equals(Parameters.E1) || parameter.equals(Parameters.E2)) {
					float val = field.getFloat(hr);
					m.setValue(new Double(val + ""));
				}
				if (parameter.getCategory().equals(ParametersConst.VOLUME)) {
					float val = field.getInt(hr);
					switch (parameter) {
					case V1:
						val = val * settings.getVolumeByImpulsSetting1();
						// System.out.println(hr.getDateTime()+" "+val);
						break;
					case V2:
						val = val * settings.getVolumeByImpulsSetting2();
						break;
					case V3:
						val = val * settings.getVolumeByImpulsSetting3();
						break;
					case V4:
						val = val * settings.getVolumeByImpulsSetting4();
						break;

					default:
						break;
					}
					m.setValue(new Double(val + ""));
				}
				if (parameter.equals(Parameters.ERROR_BYTE1) || parameter.equals(Parameters.ERROR_BYTE2)) {
					int val = field.getInt(hr);
					if (val != 129 && val != 1)
						System.out.println();
					m.setValue((double) field.getInt(hr));
				}
				measurings.add(m);
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод geHourtMeasurings() время " + ChronoUnit.NANOS.between(from, to));
		return measurings;
	}

	private void smoothHourMeasurings(List<Measuring> measurings, DayRecord dayConsumption, DayRecord sumDay) {
		LocalDateTime from = LocalDateTime.now();
		Map<Parameters, BigDecimal> multitiplicands = calculateMultiplicands(dayConsumption, sumDay);
		for (Measuring measuring : measurings) {
			Parameters parameter = measuring.getParameter();
			double value = measuring.getValue();
			BigDecimal multiplicand = multitiplicands.get(parameter);
			if (multiplicand != null) {
				BigDecimal bdValue = new BigDecimal(value + "").setScale(5, BigDecimal.ROUND_HALF_UP)
						.multiply(multiplicand).setScale(10, BigDecimal.ROUND_HALF_UP);
				measuring.setValue(bdValue.doubleValue());
			}
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод smoothHourMeasurings() время " + ChronoUnit.MILLIS.between(from, to));
	}

	private Map<Parameters, BigDecimal> calculateMultiplicands(DayRecord dayConsumption, DayRecord sumDay) {
		LocalDateTime from = LocalDateTime.now();
		Map<Parameters, BigDecimal> map = new HashMap<>();
		BigDecimal multiplicand = new BigDecimal("1");
		BigDecimal bdDay = null;
		BigDecimal bdSumDay = null;
		for (Parameters parameter : Parameters.values()) {
			switch (parameter) {
			case V1:
				bdDay = new BigDecimal(dayConsumption.getVolume1() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getVolume1() + "").setScale(5, BigDecimal.ROUND_HALF_UP);

				break;
			case V2:
				bdDay = new BigDecimal(dayConsumption.getVolume2() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getVolume2() + "").setScale(5, BigDecimal.ROUND_HALF_UP);

				break;
			case V3:
				bdDay = new BigDecimal(dayConsumption.getVolume3() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getVolume3() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				break;
			case V4:
				bdDay = new BigDecimal(dayConsumption.getVolume4() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getVolume4() + "").setScale(5, BigDecimal.ROUND_HALF_UP);

				break;
			case E1:
				// if(dayConsumption.getDate().equals(LocalDate.of(2016, 3, 2)))
				// System.out.println();
				bdDay = new BigDecimal(dayConsumption.getEnergy1() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getEnergy1() + "").setScale(5, BigDecimal.ROUND_HALF_UP);

				break;
			case E2:
				bdDay = new BigDecimal(dayConsumption.getEnergy2() + "").setScale(5, BigDecimal.ROUND_HALF_UP);
				bdSumDay = new BigDecimal(sumDay.getEnergy2() + "").setScale(5, BigDecimal.ROUND_HALF_UP);

				break;
			default:
				break;
			}
			if (bdSumDay != null && bdSumDay.doubleValue() > 0)
				multiplicand = bdDay.divide(bdSumDay, 10, BigDecimal.ROUND_HALF_UP);
			map.put(parameter, multiplicand);
		}
		LocalDateTime to = LocalDateTime.now();
		LOG.debug("Метод calculateMultiplicands() время " + ChronoUnit.MILLIS.between(from, to));
		return map;
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

}
