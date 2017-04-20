package ru.sevenkt.app.ui.handlers;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.services.ArchiveConverter;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.Parameters;

public class ImportArchiveJob extends Job {

	private IArchive archive;

	private IDBService dbService;

	private IEventBroker broker;

	public ImportArchiveJob(String name, IArchive archive, IDBService dbService, IEventBroker broker) {
		super(name);
		this.archive = archive;
		this.dbService = dbService;
		this.broker=broker;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Импорт архива " + archive.getName(), 4);
		try {
			Device device = insertOrUpdateDeviceSettings(archive.getSettings());
			ArchiveConverter ac=new ArchiveConverter(archive);
			System.out.println("Start job "+archive.getName());
			broker.send(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, device);

			monitor.subTask("Импорт месячного архива");
			dbService.insertMonthArchive(ac);
			monitor.worked(1);

			monitor.subTask("Импорт дневного архива");
			dbService.insertDayArchive(ac);
			monitor.worked(2);

			monitor.subTask("Импорт часового архива");
			dbService.insertHourArchive(ac);
			monitor.worked(3);

			monitor.subTask("Импорт журнала настроек");
			dbService.insertJournalSettings(archive, device);
			monitor.worked(4);
			
			

		} catch (Exception e) {
			MultiStatus result = new MultiStatus("ru.7kt.reader", IStatus.ERROR, e.getMessage(), e);
			StackTraceElement[] elements = e.getStackTrace();
			for (StackTraceElement stackTraceElement : elements) {
				result.addAll(new Status(IStatus.ERROR, "ru.7kt.reader", stackTraceElement.toString()));

			}
			return result;
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	private Device insertOrUpdateDeviceSettings(ISettings settings) throws Exception {
		Device device = dbService.findDeviceBySerialNum(settings.getSerialNumber());
		if (device == null) {
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
			if ((device.getFormulaNum() + "").length() > 1) {
				params.add(new Params(Parameters.AVG_TEMP3));
				params.add(new Params(Parameters.AVG_TEMP4));
				params.add(new Params(Parameters.V3));
				params.add(new Params(Parameters.V4));
				params.add(new Params(Parameters.M3));
				params.add(new Params(Parameters.M4));
			}
		} else {
			device.setDeviceVersion(settings.getDeviceVersion());
			device.setFormulaNum(settings.getFormulaNum());
			device.setNetAddress(settings.getNetAddress());
			//device.setSerialNum(settings.getSerialNumber() + "");
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
		dbService.saveDevice(device);
		return device;
	}

	private Device getDevice() {
		return null;
	}

}
