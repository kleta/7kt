
package ru.sevenkt.app.ui.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.container.Module.Settings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.app.ui.forms.ParametersModel;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.Parameters;

public class ImportFromFileHandler {

	@Inject
	private IArchiveService archiveService;

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	private Shell parentShell;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		
		this.parentShell = parentShell;
		FileDialog fd = new FileDialog(parentShell, SWT.OPEN);
		fd.setText("Открыть файл с данными");
		String[] filterExt = { "*.bin" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		LOG.info("Импорт архива "+selected);
		if (selected != null)
			try {
				IArchive archive = archiveService.readArchiveFromFile(new File(selected));
				Device device = insertOrUpdateDeviceSettings(archive.getSettings());
				if (device != null) {
					IRunnableWithProgress op = new IRunnableWithProgress() {

						@Override
						public void run(IProgressMonitor monitor)
								throws InvocationTargetException, InterruptedException {
							try {
								monitor.beginTask("Импорт данных", 4);
								
								monitor.subTask("Импорт месячного архива");
								dbService.insertMonthArchive(archive, device);
								monitor.worked(1);
								
								monitor.subTask("Импорт дневного архива");
								dbService.insertDayArchive(archive, device);
								monitor.worked(2);
								
								monitor.subTask("Импорт часового архива");
								dbService.insertHourArchive(archive, device);
								monitor.worked(3);
																
								monitor.subTask("Импорт журнала настроек");
								dbService.insertJournalSettings(archive, device);
								monitor.worked(4);
								Thread.sleep(1000);
								monitor.done();
							} catch (Exception e) {
								MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
								ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
								LOG.error(e.getMessage(), e);
							}

						}
					};
					ProgressMonitorDialog progressDialog = new TitledProgressMonitorDialog(parentShell, "Выполняется задача");
					progressDialog.run(true, true, op);

				}
			} catch (Exception e) {

				MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

				ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
				LOG.error(e.getMessage(), e);
			}

	}


	private Device insertOrUpdateDeviceSettings(ISettings settings)
			throws Exception {
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
				device.setwMax12(settings.getwMax12());
				device.setwMax34(settings.getwMax34());
				device.setwMin0(settings.getwMin0());
				device.setwMin1(settings.getwMin1());
				ArrayList<Params> params = new ArrayList<>();
				params.add(new Params(Parameters.AVG_TEMP1));
				params.add(new Params(Parameters.AVG_TEMP2));				
				params.add(new Params(Parameters.V1));
				params.add(new Params(Parameters.V2));				
				params.add(new Params(Parameters.M1));
				params.add(new Params(Parameters.M2));				
				params.add(new Params(Parameters.AVG_P1));
				params.add(new Params(Parameters.AVG_P2));
				params.add(new Params(Parameters.E1));
				params.add(new Params(Parameters.E2));
				if((device.getFormulaNum()+"").length()>1){
					params.add(new Params(Parameters.AVG_TEMP3));
					params.add(new Params(Parameters.AVG_TEMP4));
					params.add(new Params(Parameters.V3));
					params.add(new Params(Parameters.V4));
					params.add(new Params(Parameters.M3));
					params.add(new Params(Parameters.M4));
				}
				DeviceDialog dialog = new DeviceDialog(parentShell, device, new ParametersModel(params));
				dialog.create();
				if (dialog.open() == Window.OK) {
					device = dialog.getDevice();
					List<Params> selectedParams = dialog.getParams();
					device.setParams(selectedParams);
					dbService.saveDevice(device);
					broker.send(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, device);
				} else
					device = null;

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
			device.setwMax12(settings.getwMax12());
			device.setwMax34(settings.getwMax34());
			device.setwMin0(settings.getwMin0());
			device.setwMin1(settings.getwMin1());
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