
package ru.sevenkt.app.ui.handlers;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.annotations.Parameter;
import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.Archive;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.DayRecord;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.HourRecord;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.MonthRecord;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.Settings;

public class ImportFromFileHandler {

	@Inject
	private IArchiveService archiveService;

	@Inject
	private IDBService dbService;

	private Shell parentShell;

	Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		this.parentShell = parentShell;
		FileDialog fd = new FileDialog(parentShell, SWT.OPEN);
		fd.setText("Открыть файл с данными");
		String[] filterExt = { "*.bin" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		try {
			Archive archive = archiveService.readArchiveFromFile(new File(selected));
			Device device = insertOrUpdateDeviceSettings(archive.getSettings());
			if (device != null) {
				insertMonthArchive(archive, device);
				insertDayArchive(archive, device);
				insertHourArchive(archive, device);
				insertJournalSettings(archive, device);
			}
		} catch (Exception e) {

			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

			ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
			LOG.error(e.getMessage(), e);
		}

	}

	private void insertJournalSettings(Archive archive, Device device) throws Exception {

	}

	private void insertHourArchive(Archive archive, Device device) throws Exception {
		HourArchive ha = archive.getHourArchive();
		DayArchive da = archive.getDayArchive();
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime();
		LocalDateTime startArchiveDate = dateTime.minusDays(HourArchive.MAX_DAY_COUNT);
		while (!startArchiveDate.isAfter(dateTime)) {
			List<Measuring> avgMeasurings = new ArrayList<>();
			Map<Parameters, List<Measuring>> consumptionMeasuring = new HashMap<>();
			LocalDate localDate = startArchiveDate.toLocalDate();
			LOG.info("Импортируются данные за " + localDate);
			System.out.println(localDate);
			DayRecord sumDay = ha.getDayConsumption(localDate, dateTime, archive.getSettings());
			DayRecord dr2 = da.getDayRecord(localDate.plusDays(1), dateTime);
			DayRecord dr1 = da.getDayRecord(localDate, dateTime);
			DayRecord dayConsumption = dr2.minus(dr1);

			for (int i = 1; i < 25; i++) {
				LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0)).plusHours(i);
				HourRecord hr = ha.getHourRecord(localDateTime, dateTime);
				if (hr.isValid()) {
					Field[] fields = HourRecord.class.getDeclaredFields();
					for (Field field : fields) {
						if (field.isAnnotationPresent(Parameter.class)) {
							Measuring m = new Measuring();
							m.setArchiveType(ArchiveTypes.HOUR);
							m.setDateTime(hr.getDateTime());
							m.setDevice(device);
							Parameters parameter = field.getAnnotation(Parameter.class).value();
							m.setParametr(parameter);
							field.setAccessible(true);
							if (parameter.equals(Parameters.AVG_P1) || parameter.equals(Parameters.AVG_P2)) {
								float val = field.getInt(hr);
								m.setValue(val / 10);
								avgMeasurings.add(m);
							}
							if (parameter.equals(Parameters.AVG_TEMP1) || parameter.equals(Parameters.AVG_TEMP2)
									|| parameter.equals(Parameters.AVG_TEMP3)
									|| parameter.equals(Parameters.AVG_TEMP4)) {
								float val = field.getInt(hr);
								m.setValue((val / 100 > 100 ? 0 : val / 100));
								avgMeasurings.add(m);
							}

							if (parameter.equals(Parameters.E1) || parameter.equals(Parameters.E2)) {
								float val = field.getFloat(hr);
								m.setValue(val);
								List<Measuring> meas = consumptionMeasuring.get(parameter);
								if (meas == null)
									consumptionMeasuring.put(parameter, new ArrayList<>());
								consumptionMeasuring.get(parameter).add(m);
							}
							if (parameter.equals(Parameters.W1) || parameter.equals(Parameters.W2)
									|| parameter.equals(Parameters.W3) || parameter.equals(Parameters.W4)) {
								float val = field.getInt(hr);
								switch (parameter) {
								case W1:
									val = val * archive.getSettings().getVolumeByImpulsSetting1();
									break;
								case W2:
									val = val * archive.getSettings().getVolumeByImpulsSetting2();
									break;
								case W3:
									val = val * archive.getSettings().getVolumeByImpulsSetting3();
									break;
								case W4:
									val = val * archive.getSettings().getVolumeByImpulsSetting4();
									break;

								default:
									break;
								}
								m.setValue(val);
								List<Measuring> meas = consumptionMeasuring.get(parameter);
								if (meas == null)
									consumptionMeasuring.put(parameter, new ArrayList<>());
								consumptionMeasuring.get(parameter).add(m);

							}
						}
					}
				}
			}
			List<Measuring> list = avgMeasurings;
			// list.addAll(avgMeasurings);
			Set<Parameters> keySet = consumptionMeasuring.keySet();
			for (Parameters parameter : keySet) {
				List<Measuring> measurings = consumptionMeasuring.get(parameter);
				if (!dayConsumption.equalsValues(sumDay)) {
					DayRecord diffRecord = dayConsumption.minus(sumDay);
					float diffVal = 0;
					switch (parameter) {
					case W1:
						diffVal = diffRecord.getVolume1();
						break;
					case W2:
						diffVal = diffRecord.getVolume2();
						break;
					case W3:
						diffVal = diffRecord.getVolume3();
						break;
					case W4:
						diffVal = diffRecord.getVolume4();
						break;
					case E1:
						diffVal = diffRecord.getEnergy1();
						break;
					case E2:
						diffVal = diffRecord.getEnergy2();
						break;
					default:
						break;
					}
					long countNotZerroValue = measurings.stream().filter(m -> m.getValue() != 0).count();
					float addValue = diffVal / countNotZerroValue;
					measurings.stream().filter(m -> m.getValue() != 0)
							.forEach(m -> m.setValue(m.getValue() + addValue));
				}
				list.addAll(measurings);
			}
			dbService.saveMeasurings(list);
			startArchiveDate = startArchiveDate.plusDays(1);

		}
	}

	private void insertDayArchive(Archive archive, Device device) throws Exception {
		DayArchive da = archive.getDayArchive();
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime().withHour(0);
		LocalDate startArchiveDate = dateTime.minusMonths(DayArchive.MAX_MONTH_COUNT).toLocalDate();
		List<Measuring> measurings = new ArrayList<>();
		while (!startArchiveDate.isAfter(dateTime.toLocalDate())) {
			DayRecord dr = da.getDayRecord(startArchiveDate, dateTime);
			if (dr.isValid()) {
				Field[] fields = DayRecord.class.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Parameter.class)) {
						Measuring m = new Measuring();
						m.setArchiveType(ArchiveTypes.DAY);
						m.setDateTime(dr.getDate().atTime(0, 0));
						m.setDevice(device);
						Parameters parameter = field.getAnnotation(Parameter.class).value();
						m.setParametr(parameter);
						if (parameter.equals(Parameters.E1))
							System.out.println();
						field.setAccessible(true);
						float val = field.getFloat(dr);
						m.setValue(val);
						measurings.add(m);
					}
				}
			}
			startArchiveDate = startArchiveDate.plusDays(1);
		}
		dbService.saveMeasurings(measurings);

	}

	private void insertMonthArchive(Archive archive, Device device) throws Exception {
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
					if (field.isAnnotationPresent(Parameter.class)) {
						Measuring m = new Measuring();
						m.setArchiveType(ArchiveTypes.MONTH);
						m.setDateTime(mr.getDate().atTime(0, 0));
						m.setDevice(device);
						m.setParametr(field.getAnnotation(Parameter.class).value());
						field.setAccessible(true);
						float val = field.getFloat(mr);
						m.setValue(val);
						measurings.add(m);
					}
				}
			}
			startArchiveDate = startArchiveDate.plusMonths(1);
		}
		dbService.saveMeasurings(measurings);
	}

	private Device insertOrUpdateDeviceSettings(Settings settings) {
		Device device = dbService.findDeviceBySerialNum(settings.getSerialNumber());
		if (device == null) {
			MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			messageBox.setText("Новое устройство");
			messageBox.setMessage(
					"Устройство с номером " + settings.getSerialNumber() + " отсутствует в БД. Хотите добавить?");
			int retValue = messageBox.open();
			if (retValue == SWT.YES) {
				device = new Device();
				device.setDeviceName("Новое устройство");
				device.setDeviceVersion(settings.getDeviceVersion());
				device.setFormulaNum(settings.getFormulaNum());
				device.setNetAddress(settings.getNetAddress());
				device.setSerialNum(settings.getSerialNumber() + "");
				device.setTempColdWaterSetting(settings.getTempColdWaterSetting());
				device.setVolumeByImpulsSetting1(settings.getVolumeByImpulsSetting1() * 1000);
				device.setVolumeByImpulsSetting2(settings.getVolumeByImpulsSetting2() * 1000);
				device.setVolumeByImpulsSetting3(settings.getVolumeByImpulsSetting3() * 1000);
				device.setVolumeByImpulsSetting4(settings.getVolumeByImpulsSetting4() * 1000);
				DeviceDialog dialog = new DeviceDialog(parentShell, device);
				dialog.create();
				if (dialog.open() == Window.OK) {
					dbService.saveDevice(dialog.getDevice());
				}

			}
		} else {
			device.setDeviceVersion(settings.getDeviceVersion());
			device.setFormulaNum(settings.getFormulaNum());
			device.setNetAddress(settings.getNetAddress());
			device.setSerialNum(settings.getSerialNumber() + "");
			device.setTempColdWaterSetting(settings.getTempColdWaterSetting());
			device.setVolumeByImpulsSetting1(settings.getVolumeByImpulsSetting1() * 1000);
			device.setVolumeByImpulsSetting2(settings.getVolumeByImpulsSetting2() * 1000);
			device.setVolumeByImpulsSetting3(settings.getVolumeByImpulsSetting3() * 1000);
			device.setVolumeByImpulsSetting4(settings.getVolumeByImpulsSetting4() * 1000);
			dbService.saveDevice(device);
		}
		return device;
	}

	private static MultiStatus createMultiStatus(String msg, Throwable t) {

		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, "com.example.e4.rcp.todo", stackTrace.toString());
			childStatuses.add(status);
		}

		MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo", IStatus.ERROR,
				childStatuses.toArray(new Status[] {}), t.toString(), t);
		return ms;
	}

}