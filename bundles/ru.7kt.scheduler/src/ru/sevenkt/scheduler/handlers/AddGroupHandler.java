
package ru.sevenkt.scheduler.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.quartz.SchedulerException;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.scheduler.SchedulerEventConstants;
import ru.sevenkt.scheduler.dialogs.SchedulerData;
import ru.sevenkt.scheduler.dialogs.SchedulerGroupDialog;
import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class AddGroupHandler {

	@Inject
	private IDBService dbService;

	@Inject
	private IEventBroker broker;

	@Inject
	@Optional
	ISchedulerSevice schService;

	public AddGroupHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Execute
	public void execute(Shell shell) throws SchedulerException {
		List<Device> devices = dbService.findAllDevices();
		SchedulerData data = new SchedulerData("0", "0", "*", "*", "?");
		SchedulerGroupDialog dialog = new SchedulerGroupDialog(shell, data, devices);
		if (dialog.open() == Window.OK) {
			data = dialog.getSchedulerData();
			SchedulerGroup sg = mapToSchedulerGroup(data);
			dbService.saveSchedulerGroup(sg);
			broker.post(SchedulerEventConstants.TOPIC_REFRESH_SCHEDULER_VIEW, sg);
			schService.restart();
		}

	}

	private SchedulerGroup mapToSchedulerGroup(SchedulerData data) {
		SchedulerGroup sg = new SchedulerGroup();
		sg.setCronString(data.generateCronExpression());
		sg.setDeepDay(data.getDayShift());
		sg.setName(data.getName());
		sg.setArchiveTypes(new ArrayList<>());
		if (data.isDayCheck())
			sg.getArchiveTypes().add(new ArchiveType(ArchiveTypes.DAY));
		if (data.isHourCheck())
			sg.getArchiveTypes().add(new ArchiveType(ArchiveTypes.HOUR));
		if (data.isMonthCheck())
			sg.getArchiveTypes().add(new ArchiveType(ArchiveTypes.MONTH));
		List<Device> selectedDevice = data.getSelectedDevice();
		if (selectedDevice != null) {
			sg.setDevices(selectedDevice);
			for (Device device : selectedDevice) {
				Set<SchedulerGroup> groups = device.getGroups();
				if (groups == null) {
					groups = new HashSet<>();
					device.setGroups(groups);
				}
				device.getGroups().add(sg);
			}
		}
		return sg;
	}

}