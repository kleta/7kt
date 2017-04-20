package ru.sevenkt.db.services;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
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

	// private Map<LocalDateTime, Map<,BigDecimal>> monthError;

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

			if (recordDate.isAfter(LocalDate.parse("2015-12-30")))
				System.out.println();
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
			if(valDay==null)
				multiplicands=new HashMap<>();
			else
				multiplicands = calculateMultiplicands(sumDay, valPrevDay, valDay);		
			List<IHourRecord> hrs = ha.getRecordsByDay(recordDate);
			for (IHourRecord hr : hrs) {
				if (hr!=null && hr.isValid()) {
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
		if(valDay==null)
			System.out.println();
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

}
