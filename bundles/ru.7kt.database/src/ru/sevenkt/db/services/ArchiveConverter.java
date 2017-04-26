package ru.sevenkt.db.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.h2.constant.ErrorCode;

import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.IDayRecord;
import ru.sevenkt.domain.IHourRecord;
import ru.sevenkt.domain.IMonthRecord;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class ArchiveConverter {

	private Device device;

	private IArchive archive;

	private Map<LocalDate, Map<Parameters, BigDecimal>> monthData;

	private Map<LocalDate, Map<Parameters, BigDecimal>> dayData;

	private Map<LocalDateTime, Map<Parameters, BigDecimal>> hourData;

	private Map<LocalDate, Set<ErrorCode>> monthErrors;

	private Map<LocalDate, Set<ErrorCode>> dayErrors;

	private Map<LocalDateTime, Set<ErrorCodes>> hourErrors;

	private List<Journal> journalData;

	public ArchiveConverter(IArchive archive) {
		this.archive = archive;
		ISettings settings = archive.getSettings();
		device = new Device();
		device.setDeviceVersion(settings.getDeviceVersion());
		device.setFormulaNum(settings.getFormulaNum());
		device.setNetAddress(settings.getNetAddress());
		device.setSerialNum(settings.getSerialNumber() + "");
		device.setTempColdWaterSetting(settings.getTempColdWaterSetting());
		device.setVolumeByImpulsSetting1(settings.getVolumeByImpulsSetting1() * 1000);
		device.setVolumeByImpulsSetting2(settings.getVolumeByImpulsSetting2() * 1000);
		device.setVolumeByImpulsSetting3(settings.getVolumeByImpulsSetting3() * 1000);
		device.setVolumeByImpulsSetting4(settings.getVolumeByImpulsSetting4() * 1000);
		device.setwMax12(settings.getwMax12());
		device.setwMax34(settings.getwMax34());
		device.setwMin0(settings.getwMin0());
		device.setwMin1(settings.getwMin1());
	}

	public List<Parameters> getAccountParameters(int fNum) {
		int firstInput = fNum % 10;
		List<Parameters> params = new ArrayList<>(Arrays.asList(new Parameters[] { Parameters.AVG_TEMP1 }));
		switch (firstInput) {
		case 1:
			params.add(Parameters.AVG_TEMP2);
			params.add(Parameters.V1);
			params.add(Parameters.V2);
			break;
		case 2:
			params.add(Parameters.AVG_TEMP2);
			params.add(Parameters.V1);
			break;
		case 3:
			params.add(Parameters.AVG_TEMP2);
			params.add(Parameters.V2);
			break;
		case 5:
			params.add(Parameters.V1);
			params.add(Parameters.V2);
			break;
		case 6:
			params.add(Parameters.V1);
			break;
		}
		params.add(Parameters.AVG_P1);
		params.add(Parameters.E1);
		if (fNum > 10) {
			params.add(Parameters.AVG_TEMP3);
			int secondInput = fNum / 10;
			switch (secondInput) {
			case 1:
				params.add(Parameters.AVG_TEMP4);
				params.add(Parameters.V3);
				params.add(Parameters.V4);
				params.add(Parameters.AVG_P2);
				params.add(Parameters.E2);
				break;
			case 2:
				params.add(Parameters.AVG_TEMP4);
				params.add(Parameters.V3);
				params.add(Parameters.AVG_P2);
				params.add(Parameters.E2);
				break;
			case 3:
				params.add(Parameters.AVG_TEMP4);
				params.add(Parameters.V4);
				params.add(Parameters.AVG_P2);
				params.add(Parameters.E2);
				break;
			case 5:
				params.add(Parameters.V3);
				params.add(Parameters.V4);
				params.add(Parameters.AVG_P2);
				params.add(Parameters.E2);
				break;
			case 6:
				params.add(Parameters.V3);
				params.add(Parameters.AVG_P2);
				params.add(Parameters.E2);
				break;
			case 4:
				params.add(Parameters.V3);
				break;
			}

		}
		if (archive.getSettings().getDeviceVersion() == 4) {
			params.add(Parameters.AVG_P3);
			params.add(Parameters.AVG_P4);
		}

		return params;
	}

	private void initMonthData() throws Exception {
		LocalDateTime archiveDateTime = archive.getCurrentData().getCurrentDateTime();
		MonthArchive ma = archive.getMonthArchive();
		long monthDeep = ma.getMaxYearsDeep();
		LocalDate recordDate = archiveDateTime.withDayOfMonth(1).minusYears(monthDeep).toLocalDate();
		monthData = new HashMap<>();
		while (recordDate.isBefore(archiveDateTime.toLocalDate())) {
			IMonthRecord mr = ma.getMonthRecord(recordDate);
			if (mr != null && mr.isValid()) {
				Field[] fields = mr.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Parameter.class)) {
						Parameters parameter = field.getAnnotation(Parameter.class).value();
						Map<Parameters, BigDecimal> pData;
						switch (parameter.getCategory()) {
						case ParametersConst.ENERGY:
						case ParametersConst.VOLUME:
						case ParametersConst.WEIGHT:
							pData = monthData.get(recordDate);
							if (pData == null) {
								pData = new HashMap<>();
								monthData.put(recordDate, pData);
							}
							float val = field.getFloat(mr);
							pData.put(parameter, new BigDecimal(val + ""));

						}
					}
				}

			}
			recordDate = recordDate.plusMonths(1);
		}
	}

	private void initDayData() throws Exception {
		LocalDateTime archiveDateTime = archive.getCurrentData().getCurrentDateTime();
		DayArchive da = archive.getDayArchive();
		long monthDeep = da.getMaxMonthDeep();
		LocalDate recordDate = archiveDateTime.minusMonths(monthDeep).toLocalDate();
		dayData = new HashMap<>();
		while (recordDate.isBefore(archiveDateTime.toLocalDate())) {
			IDayRecord dr = da.getDayRecord(recordDate);
			if (dr != null && dr.isValid()) {
				Field[] fields = dr.getClass().getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Parameter.class)) {
						Parameters parameter = field.getAnnotation(Parameter.class).value();
						Map<Parameters, BigDecimal> pData = dayData.get(recordDate);
						switch (parameter.getCategory()) {
						case ParametersConst.ENERGY:
						case ParametersConst.VOLUME:
						case ParametersConst.WEIGHT: {
							if (pData == null) {
								pData = new HashMap<>();
								dayData.put(recordDate, pData);
							}
							float val = field.getFloat(dr);
							pData.put(parameter, new BigDecimal(val + ""));

							break;
						}
						case ParametersConst.PRESSURE: {
							if (pData == null) {
								pData = new HashMap<>();
								dayData.put(recordDate, pData);
							}
							float val = field.getInt(dr) / 10;
							pData.put(parameter, new BigDecimal(val + ""));
							break;
						}
						case ParametersConst.TEMP:
							if (pData == null) {
								pData = new HashMap<>();
								dayData.put(recordDate, pData);
							}
							float val = field.getInt(dr) / 100;
							if (val > -60 && val < 150) {
								pData.put(parameter, new BigDecimal(val + ""));
							}
							break;
						}

					}
				}

			}
			recordDate = recordDate.plusDays(1);
		}
	}

	private void initHourData() throws Exception {
		LocalDateTime archiveDateTime = archive.getCurrentData().getCurrentDateTime();
		HourArchive ha = archive.getHourArchive();
		long daysDeep = ha.getMaxDaysDeep();
		LocalDate recordDate = archiveDateTime.minusDays(daysDeep).toLocalDate();
		hourData = new HashMap<>();
		while (recordDate.isBefore(archiveDateTime.toLocalDate().plusDays(1))) {
			Map<Parameters, BigDecimal> sumDay = ha.getSumByDay(recordDate);
			Map<Parameters, BigDecimal> valDay = dayData.get(recordDate.plusDays(1));
			Map<Parameters, BigDecimal> valPrevDay = dayData.get(recordDate);
			Map<Parameters, BigDecimal> multiplicands;
			if (valDay == null || valPrevDay == null)
				multiplicands = new HashMap<>();
			else
				multiplicands = calculateMultiplicands(sumDay, valPrevDay, valDay);
			List<IHourRecord> hrs = ha.getRecordsByDay(recordDate);
			for (IHourRecord hr : hrs) {
				if (hr != null && hr.isValid()) {
					Field[] fields = hr.getClass().getDeclaredFields();
					for (Field field : fields) {
						field.setAccessible(true);
						if (field.isAnnotationPresent(Parameter.class)) {
							Parameters parameter = field.getAnnotation(Parameter.class).value();
							Map<Parameters, BigDecimal> pData = hourData.get(hr.getDateTime());
							float val;
							switch (parameter) {
							case V1: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getFloat(hr) * archive.getSettings().getVolumeByImpulsSetting1();
								BigDecimal mult = multiplicands.get(parameter);
								if (mult != null) {
									BigDecimal bdVal = new BigDecimal(val + "").multiply(mult);
									pData.put(parameter, bdVal);
								} else
									pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case V2: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getFloat(hr) * archive.getSettings().getVolumeByImpulsSetting2();
								BigDecimal mult = multiplicands.get(parameter);
								if (mult != null) {
									BigDecimal bdVal = new BigDecimal(val + "").multiply(mult);
									pData.put(parameter, bdVal);
								} else
									pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case V3: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getFloat(hr) * archive.getSettings().getVolumeByImpulsSetting3();
								BigDecimal mult = multiplicands.get(parameter);
								if (mult != null) {
									BigDecimal bdVal = new BigDecimal(val + "").multiply(mult);
									pData.put(parameter, bdVal);
								} else
									pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case V4: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getFloat(hr) * archive.getSettings().getVolumeByImpulsSetting4();
								BigDecimal mult = multiplicands.get(parameter);
								if (mult != null) {
									BigDecimal bdVal = new BigDecimal(val + "").multiply(mult);
									pData.put(parameter, bdVal);
								} else
									pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case E1:
							case E2:
							case M1:
							case M2:
							case M3:
							case M4: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getFloat(hr);
								BigDecimal mult = multiplicands.get(parameter);
								if (mult != null) {
									BigDecimal bdVal = new BigDecimal(val + "").multiply(mult);
									pData.put(parameter, bdVal);
								} else
									pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case AVG_P1:
							case AVG_P2:
							case AVG_P3:
							case AVG_P4: {
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getInt(hr) / 10;
								pData.put(parameter, new BigDecimal(val + ""));
								break;
							}
							case AVG_TEMP1:
							case AVG_TEMP2:
							case AVG_TEMP3:
							case AVG_TEMP4:
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getInt(hr) / 100;
								if (val > -60 && val < 150) {
									pData.put(parameter, new BigDecimal(val + ""));
								}
								break;
							case ERROR_BYTE1:
							case ERROR_BYTE2:
								if (pData == null) {
									pData = new HashMap<>();
									hourData.put(hr.getDateTime(), pData);
								}
								val = field.getInt(hr);
								pData.put(parameter, new BigDecimal(val + ""));
								break;
							default:
								break;
							}

						}
					}
				}
			}
			recordDate = recordDate.plusDays(1);
		}
	}

	private Map<Parameters, BigDecimal> calculateMultiplicands(Map<Parameters, BigDecimal> sumDay,
			Map<Parameters, BigDecimal> valPrevDay, Map<Parameters, BigDecimal> valDay) {
		Map<Parameters, BigDecimal> map = new HashMap<>();
		for (Parameters parameter : sumDay.keySet()) {
			BigDecimal pSumVal = sumDay.get(parameter);
			BigDecimal pPrevDayVal = valPrevDay.get(parameter);
			BigDecimal pDayVal = valDay.get(parameter);

			if (pPrevDayVal != null) {
				if (!(pSumVal.compareTo(BigDecimal.ZERO) == 0)) {
					float impuls;
					switch (parameter) {
					case V1:
						impuls = archive.getSettings().getVolumeByImpulsSetting1();
						pSumVal = pSumVal.multiply(new BigDecimal("" + impuls));
						break;
					case V2:
						impuls = archive.getSettings().getVolumeByImpulsSetting2();
						pSumVal = pSumVal.multiply(new BigDecimal("" + impuls));
						break;
					case V3:
						impuls = archive.getSettings().getVolumeByImpulsSetting3();
						pSumVal = pSumVal.multiply(new BigDecimal("" + impuls));
						break;
					case V4:
						impuls = archive.getSettings().getVolumeByImpulsSetting4();
						pSumVal = pSumVal.multiply(new BigDecimal("" + impuls));
						break;
					default:
						break;
					}
					BigDecimal sub = pDayVal.subtract(pPrevDayVal);
					BigDecimal mult = sub.divide(pSumVal, 32, BigDecimal.ROUND_HALF_UP);
					map.put(parameter, mult);
				}
			}

		}
		return map;
	}

	public List<Measuring> getMonthData() throws Exception {
		List<Measuring> mList = new ArrayList<>();
		if (monthData == null)
			initMonthData();
		for (LocalDate date : monthData.keySet()) {
			Map<Parameters, BigDecimal> pData = monthData.get(date);
			for (Parameters p : pData.keySet()) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.MONTH);
				m.setDateTime(date.atStartOfDay());
				m.setDevice(device);
				m.setParameter(p);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(pData.get(p));
				mList.add(m);
			}
		}
		return mList;
	}

	public List<Measuring> getDayData() throws Exception {
		List<Measuring> mList = new ArrayList<>();
		if (dayData == null)
			initDayData();
		for (LocalDate date : dayData.keySet()) {
			Map<Parameters, BigDecimal> pData = dayData.get(date);
			for (Parameters p : pData.keySet()) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.DAY);
				m.setDateTime(date.atStartOfDay());
				m.setDevice(device);
				m.setParameter(p);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(pData.get(p));
				mList.add(m);
			}
		}
		return mList;
	}

	public List<Measuring> getHourData() throws Exception {
		List<Measuring> mList = new ArrayList<>();
		if (dayData == null)
			initDayData();
		if (hourData == null)
			initHourData();
		for (LocalDateTime date : hourData.keySet()) {
			Map<Parameters, BigDecimal> pData = hourData.get(date);
			for (Parameters p : pData.keySet()) {
				Measuring m = new Measuring();
				m.setArchiveType(ArchiveTypes.HOUR);
				m.setDateTime(date);
				m.setDevice(device);
				m.setParameter(p);
				m.setTimestamp(LocalDateTime.now());
				m.setValue(pData.get(p));
				mList.add(m);
			}
		}
		return mList;
	}

	public List<Error> getHourErrors() throws Exception {
		List<Error> errors = new ArrayList<>();
		if (hourData == null) {
			initDayData();
			initHourData();
		}
		if (hourErrors == null)
			initHourErrors();
		for (LocalDateTime dateTime : hourErrors.keySet()) {
			Set<ErrorCodes> codes = hourErrors.get(dateTime);
			for (ErrorCodes errorCodes : codes) {
				Error error = new Error();
				error.setArchiveType(ArchiveTypes.HOUR);
				error.setDateTime(dateTime);
				error.setDevice(device);
				error.setErrorCode(errorCodes);
				errors.add(error);
			}
		}
		return errors;
	}

	private void initHourErrors() {
		hourErrors = new HashMap<>();
		float min11 = archive.getSettings().getwMin0() * archive.getSettings().getVolumeByImpulsSetting1();
		float min21 = archive.getSettings().getwMin0() * archive.getSettings().getVolumeByImpulsSetting2();
		
		

		float min32 = archive.getSettings().getwMin1() * archive.getSettings().getVolumeByImpulsSetting3();
		float min42 = archive.getSettings().getwMin1() * archive.getSettings().getVolumeByImpulsSetting4();

		min11=min21=min32=min42=0;
		
		float max11 = archive.getSettings().getwMax12() * archive.getSettings().getVolumeByImpulsSetting1();
		float max21 = archive.getSettings().getwMax12() * archive.getSettings().getVolumeByImpulsSetting2();

		float max32 = archive.getSettings().getwMax34() * archive.getSettings().getVolumeByImpulsSetting3();
		float max42 = archive.getSettings().getwMax34() * archive.getSettings().getVolumeByImpulsSetting4();
		Set<ErrorCodes> errorSet = null;
		for (LocalDateTime dateTime : hourData.keySet()) {
			int firstInput = archive.getSettings().getFormulaNum() % 10;
			errorSet = hourErrors.get(dateTime);
			if (errorSet == null) {
				errorSet = new HashSet<>();
			}
			BigDecimal t1 = hourData.get(dateTime).get(Parameters.AVG_TEMP1);
			BigDecimal t2 = hourData.get(dateTime).get(Parameters.AVG_TEMP2);
			BigDecimal v1 = hourData.get(dateTime).get(Parameters.V1);
			BigDecimal v2 = hourData.get(dateTime).get(Parameters.V2);
			BigDecimal t3 = hourData.get(dateTime).get(Parameters.AVG_TEMP3);
			BigDecimal t4 = hourData.get(dateTime).get(Parameters.AVG_TEMP4);
			BigDecimal v3 = hourData.get(dateTime).get(Parameters.V3);
			BigDecimal v4 = hourData.get(dateTime).get(Parameters.V4);
			switch (firstInput) {
			case 1: {

				if (t1 == null || t2 == null) {

					errorSet.add(ErrorCodes.T1);
				} else {

					if (t1.doubleValue() < t2.doubleValue())
						errorSet.add(ErrorCodes.E1);
				}
				if (v1 != null && v2 != null) {

					if (v1.doubleValue() == 0 || v1.doubleValue() < min11 || v1.doubleValue() > max11
							|| v2.doubleValue() == 0 || v2.doubleValue() < min21 || v2.doubleValue() > max21)
						errorSet.add(ErrorCodes.V1);
					if (v1.doubleValue() * 1.2 < v2.doubleValue())
						errorSet.add(ErrorCodes.E1);
				}

				break;
			}
			case 2: {

				if (t1 == null || t2 == null) {

					errorSet.add(ErrorCodes.T1);
				} else {

					if (t1.doubleValue() < t2.doubleValue())
						errorSet.add(ErrorCodes.E1);
				}
				if (v1 != null) {
					if (v1.doubleValue() == 0 || v1.doubleValue() < min11 || v1.doubleValue() > max11)
						errorSet.add(ErrorCodes.V1);
				}
				break;
			}
			case 3: {

				if (t1 == null || t2 == null) {

					errorSet.add(ErrorCodes.T1);
				} else {

					if (t1.doubleValue() < t2.doubleValue())
						errorSet.add(ErrorCodes.E1);
				}
				if (v2 != null) {

					if (v2.doubleValue() == 0 || v2.doubleValue() < min21 || v2.doubleValue() > max21)
						errorSet.add(ErrorCodes.V1);
				}

				break;
			}
			case 5: {

				if (t1 == null) {

					errorSet.add(ErrorCodes.T1);
				}
				if (v1 != null && v2 != null) {
					if (v1.doubleValue() == 0 || v1.doubleValue() < min11 || v1.doubleValue() > max11
							|| v2.doubleValue() == 0 || v2.doubleValue() < min21 || v2.doubleValue() > max21)
						errorSet.add(ErrorCodes.V1);
					if (v1.doubleValue() * 1.2 < v2.doubleValue())
						errorSet.add(ErrorCodes.E1);
				}

				break;
			}
			case 6: {

				if (t1 == null) {
					errorSet.add(ErrorCodes.T1);
				}
				break;
			}
			}

			if (archive.getSettings().getFormulaNum() > 10) {
				int secondInput = archive.getSettings().getFormulaNum() / 10;
				switch (secondInput) {
				case 1: {

					if (t3 == null || t4 == null) {

						errorSet.add(ErrorCodes.T2);
					} else {

						if (t3.doubleValue() < t4.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}
					if (v3 != null && v4 != null) {

						if (v3.doubleValue() == 0 || v3.doubleValue() < min32 || v3.doubleValue() > max32
								|| v4.doubleValue() == 0 || v4.doubleValue() < min42 || v4.doubleValue() > max42)
							errorSet.add(ErrorCodes.V2);
						if (v3.doubleValue() * 1.2 < v4.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}

					break;
				}
				case 2: {

					if (t3 == null || t4 == null) {

						errorSet.add(ErrorCodes.T2);
					} else {
						errorSet = hourErrors.get(dateTime);
						if (errorSet == null) {
							errorSet = new HashSet<>();
						}
						if (t3.doubleValue() < t4.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}
					if (v3 != null) {

						if (v3.doubleValue() == 0 || v3.doubleValue() < min32 || v3.doubleValue() > max32)
							errorSet.add(ErrorCodes.V2);
					}

					break;
				}
				case 3: {

					if (t3 == null || t4 == null) {

						errorSet.add(ErrorCodes.T2);
					} else {

						if (t3.doubleValue() < t4.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}
					if (v4 != null) {

						if (v4.doubleValue() == 0 || v4.doubleValue() < min42 || v4.doubleValue() > max42)
							errorSet.add(ErrorCodes.V2);
					}

					break;
				}
				case 5: {

					if (t3 == null) {

						errorSet.add(ErrorCodes.T2);
					}
					if (v3 != null && v4 != null) {
						if (v3.doubleValue() == 0 || v3.doubleValue() < min32 || v3.doubleValue() > max32
								|| v4.doubleValue() == 0 || v4.doubleValue() < min42 || v4.doubleValue() > max42)
							errorSet.add(ErrorCodes.V2);
						if (v3.doubleValue() * 1.2 < v4.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}

					break;
				}
				case 6: {

					if (t3 == null) {

						errorSet.add(ErrorCodes.T2);
					}

					break;
				}
				case 4: {

					if (t1 == null || t2 == null || t3 == null) {

						if (t3 == null)
							errorSet.add(ErrorCodes.T2);
						errorSet.add(ErrorCodes.T1);
					} else {

						if (t1.doubleValue() < t2.doubleValue())
							errorSet.add(ErrorCodes.E1);
						if (t2.doubleValue() < t3.doubleValue())
							errorSet.add(ErrorCodes.E2);
					}
					if (v1 != null && v2 != null) {

						if (v1.doubleValue() == 0 || v1.doubleValue() < min11 || v1.doubleValue() > max11
								|| v2.doubleValue() == 0 || v2.doubleValue() < min21 || v2.doubleValue() > max21)
							errorSet.add(ErrorCodes.V1);
						if (v1.doubleValue() * 1.2 < v2.doubleValue())
							errorSet.add(ErrorCodes.E1);
					}

					break;
				}
				}

			}
			byte errorByte1 = hourData.get(dateTime).get(Parameters.ERROR_BYTE1).byteValue();
			int errorUisExist = errorByte1 & 0x80;
//			if (v1.doubleValue() > 0){
//				System.out.println(dateTime+" "+t1 + " " + t2 + " " + t3 + " " + t4 + " " + v1 + " " + v2 + " " + v3 + " " + v4);
//				System.out.println(errorSet);
//			}
			if (!errorSet.isEmpty()) {
				if (errorUisExist != 0)
					errorSet.add(ErrorCodes.U);
				if (errorSet.contains(ErrorCodes.E1) || errorSet.contains(ErrorCodes.V1)
						|| errorSet.contains(ErrorCodes.T1)) {
					hourData.get(dateTime).put(Parameters.ERROR_TIME1, new BigDecimal("1"));
				}
				if (errorSet.contains(ErrorCodes.E2) || errorSet.contains(ErrorCodes.V2)
						|| errorSet.contains(ErrorCodes.T2)) {
					hourData.get(dateTime).put(Parameters.ERROR_TIME2, new BigDecimal("1"));
				}
				hourErrors.put(dateTime, errorSet);
			} else if (errorUisExist != 0) {
				errorSet.add(ErrorCodes.U);
				hourErrors.put(dateTime, errorSet);
			}
		}
	}

	public Device getDevice() {
		return device;
	}

}
