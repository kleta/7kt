package ru.sevenkt.app.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;

import ru.sevenkt.app.AppEventConstants;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.ArchiveConverter;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.Parameters;

public class ImportArchiveJob extends Job {

	private IArchive archive;

	@Inject
	private IDBService dbService;

	@Inject
	private IEventBroker broker;

	public ImportArchiveJob() {
		super("Импорт архива");
		// this.archive = archive;
		// this.dbService = dbService;
		// this.broker=broker;
	}

	public IArchive getArchive() {
		return archive;
	}

	public void setArchive(IArchive archive) {
		this.archive = archive;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Импорт архива " + archive.getName(), 4);
		try {
			// dbService.initEM();
			ArchiveConverter ac = new ArchiveConverter(archive);
			Device device = ac.getDevice();
			List<Parameters> parameters = ac.getDeviceParameters(device.getFormulaNum());
			device=insertOrUpdateDeviceSettings(device, parameters);
			ac.setDevice(device);
			System.out.println("Start job " + archive.getName());
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
			List<Status> childStatuses = new ArrayList<>();
			StackTraceElement[] stackTraces = e.getStackTrace();
			for (StackTraceElement stackTrace : stackTraces) {
				Status status = new Status(IStatus.ERROR, "ru.7kt.app", stackTrace.toString());
				childStatuses.add(status);
			}
			MultiStatus ms = new MultiStatus("ru.7kt.app", IStatus.ERROR, childStatuses.toArray(new Status[] {}),
					"Произошла ошибка при импорте " + archive.getName(), e);
			return ms;
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	private Device insertOrUpdateDeviceSettings(Device device, List<Parameters> parameters) throws Exception {
		Device d = dbService.findDeviceBySerialNum(Integer.parseInt(device.getSerialNum()));
		if (d == null) {
			d = new Device(device);
			ArrayList<Params> params = new ArrayList<>();
			for (Parameters par : parameters) {
				params.add(new Params(par));
			}
			d.setDeviceName("Тепловычислитель");
			d.setParams(params);
		} else {
			d.setDeviceVersion(device.getDeviceVersion());
			d.setFormulaNum(device.getFormulaNum());
			d.setNetAddress(device.getNetAddress());
			d.setTempColdWaterSetting(device.getTempColdWaterSetting());
			d.setVolumeByImpulsSetting1(device.getVolumeByImpulsSetting1());
			d.setVolumeByImpulsSetting2(device.getVolumeByImpulsSetting2());
			d.setVolumeByImpulsSetting3(device.getVolumeByImpulsSetting3());
			d.setVolumeByImpulsSetting4(device.getVolumeByImpulsSetting4());
			d.setwMax12(device.getwMax12());
			d.setwMax34(device.getwMax34());
			d.setwMin0(device.getwMin0());
			d.setwMin1(device.getwMin1());
		}
		dbService.saveDevice(d);
		return d;
	}

	private Device getDevice() {
		return null;
	}

}
