
package ru.sevenkt.scheduler.handlers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.quartz.SchedulerException;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.scheduler.Group;
import ru.sevenkt.scheduler.SchedulerEventConstants;
import ru.sevenkt.scheduler.dialogs.SchedulerData;
import ru.sevenkt.scheduler.dialogs.SchedulerGroupDialog;
import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class EditHandler implements EventHandler {
	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	private SchedulerGroup currentSelection;

	private Shell shell;
	
	@Inject 
	@Optional
	ISchedulerSevice schService;

	@PostConstruct
	public void init() {
		broker.subscribe(SchedulerEventConstants.TOPIC_EDIT_SCHEDULER, this);
	}
	@Execute
	public void execute() throws SchedulerException  {
		List<Device> devices = dbService.findAllDevices();
		SchedulerData dataOld = new SchedulerData(currentSelection);
		SchedulerGroupDialog dialog = new SchedulerGroupDialog(shell, dataOld, devices);
		if (dialog.open() == Window.OK) {
			SchedulerData data = dialog.getSchedulerData();
			SchedulerGroup sg = mapToSchedulerGroup(data);
			dbService.saveSchedulerGroup(sg);
			broker.post(SchedulerEventConstants.TOPIC_REFRESH_SCHEDULER_VIEW, sg);
			schService.restart();
		}
	}

	@Override
	public void handleEvent(Event event) {
	
			try {
				execute();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Inject
	public void select(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Group gr,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		if (gr != null)
			currentSelection = gr.getSchedulerGroup();
		shell = parentShell;
	}

	private SchedulerGroup mapToSchedulerGroup(SchedulerData data) {
		SchedulerGroup sg = currentSelection;
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
		sg.setDevices(selectedDevice);
		for (Device device : selectedDevice) {
			Set<SchedulerGroup> groups = device.getGroups();
			if(groups==null){
				groups=new HashSet<>();
				device.setGroups(groups);
			}
			device.getGroups().add(sg);
		}
		return sg;
	}
}