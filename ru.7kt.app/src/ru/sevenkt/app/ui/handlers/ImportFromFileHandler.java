
package ru.sevenkt.app.ui.handlers;

import java.io.File;
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

import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.archive.domain.Archive;
import ru.sevenkt.archive.domain.MonthArchive;
import ru.sevenkt.archive.domain.MonthRecord;
import ru.sevenkt.archive.domain.Settings;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.IDBService;

public class ImportFromFileHandler {

	@Inject
	IArchiveService archiveService;

	@Inject
	IDBService dbService;

	Shell parentShell;

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
			insertOrUpdateDeviceSettings(archive.getSettings());
			insertMonthArchive(archive);
			insertDayArchive(archive);
			insertHourArchive(archive);
			insertJournalSettings(archive);
		} catch (Exception e) {

			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

			ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
			LOG.error(e.getMessage(), e);
		}

	}

	private void insertJournalSettings(Archive archive) {
		
	}

	private void insertHourArchive(Archive archive) throws Exception {
		
	}

	private void insertDayArchive(Archive archive) {
		// TODO Auto-generated method stub		
	}

	private void insertMonthArchive(Archive archive) throws Exception {
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
				Measuring m=new Measuring();
			}
			startArchiveDate=startArchiveDate.plusMonths(1);
		}
		
	}

	private void insertOrUpdateDeviceSettings(Settings settings) {
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