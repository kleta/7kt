
package ru.sevenkt.app.ui.handlers;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import ru.sevenkt.annotation.Parameter;
import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.Archive;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.domain.MonthRecord;
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
		this.parentShell=parentShell;
		FileDialog fd = new FileDialog(parentShell, SWT.OPEN);
		fd.setText("Открыть файл с данными");
		String[] filterExt = { "*.bin" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		try {
			Archive archive = archiveService.readArchiveFromFile(new File(selected));
			Device device = insertOrUpdateDeviceSettings(archive.getSettings());
			insertMonthArchive(archive, device);
			insertDayArchive(archive, device);
			insertHourArchive(archive, device);
			insertJournalSettings(archive);
		} catch (Exception e) {

			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

			ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
			LOG.error(e.getMessage(), e);
		}

	}

	private void insertJournalSettings(Archive archive) {
		
	}

	private void insertHourArchive(Archive archive, Device device) throws Exception {
		
	}

	private void insertDayArchive(Archive archive, Device device) {
		// TODO Auto-generated method stub		
	}

	private void insertMonthArchive(Archive archive, Device device) throws Exception {
		MonthArchive ma = archive.getMonthArchive();	
		LocalDateTime dateTime = archive.getCurrentData().getCurrentDateTime();
		LocalDate startArchiveDate = dateTime.minusYears(MonthArchive.MAX_YEAR_COUNT).toLocalDate();
		while(startArchiveDate.isBefore(dateTime.toLocalDate())){
			int year = startArchiveDate.getYear()-2000;
			if(year<15){
				startArchiveDate=startArchiveDate.plusMonths(1);
				continue;
			}
			MonthRecord mr = ma.getMonthRecord(year, startArchiveDate.getMonthValue());
			if(mr.isValid()){		
				Field[] fields = MonthRecord.class.getDeclaredFields();
				for (Field field : fields) {
					if(field.isAnnotationPresent(Parameter.class)){
						Measuring m=new Measuring();
						m.setArchiveType(ArchiveTypes.MONTH);
						m.setDateTime(mr.getDate().atTime(0,0));
						m.setDevice(device);
						m.setParametr(field.getAnnotation(Parameter.class).value());
						field.setAccessible(true);
						float val=field.getFloat(mr);
						m.setValue(val);
						dbService.saveMeasuring(m);
					}
				}
			}
			startArchiveDate=startArchiveDate.plusMonths(1);
		}
		
	}

	private Device insertOrUpdateDeviceSettings(Settings settings) {
		Device device = dbService.findDeviceBySerialNum(settings.getSerialNumber());
		if (device == null) {
			MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			messageBox.setText("Новое устройство");
			messageBox.setMessage(
					"Устройство с номером " + settings.getSerialNumber() + " отсутствует в БД. Хотите добавить?");
			int retValue = messageBox.open();
			if(retValue==SWT.YES){
				device=new Device();
				device.setDeviceName("Новое устройство");
				device.setDeviceVersion(settings.getDeviceVersion());
				device.setFormulaNum(settings.getFormulaNum());
				device.setNetAddress(settings.getNetAddress());
				device.setSerialNum(settings.getSerialNumber()+"");
				device.setTempColdWaterSetting(settings.getTempColdWaterSetting());
				device.setVolumeByImpulsSetting1(settings.getVolumeByImpulsSetting1()*1000);
				device.setVolumeByImpulsSetting2(settings.getVolumeByImpulsSetting2()*1000);
				device.setVolumeByImpulsSetting3(settings.getVolumeByImpulsSetting3()*1000);
				device.setVolumeByImpulsSetting4(settings.getVolumeByImpulsSetting4()*1000);
				DeviceDialog dialog=new DeviceDialog(parentShell, device);
				dialog.create();
				if (dialog.open() == Window.OK) {
					dbService.saveDevice(dialog.getDevice());
				} 


			}
		}
		else{
			device.setDeviceVersion(settings.getDeviceVersion());
			device.setFormulaNum(settings.getFormulaNum());
			device.setNetAddress(settings.getNetAddress());
			device.setSerialNum(settings.getSerialNumber()+"");
			device.setTempColdWaterSetting(settings.getTempColdWaterSetting());
			device.setVolumeByImpulsSetting1(settings.getVolumeByImpulsSetting1()*1000);
			device.setVolumeByImpulsSetting2(settings.getVolumeByImpulsSetting2()*1000);
			device.setVolumeByImpulsSetting3(settings.getVolumeByImpulsSetting3()*1000);
			device.setVolumeByImpulsSetting4(settings.getVolumeByImpulsSetting4()*1000);
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