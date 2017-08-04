
package ru.sevenkt.app.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.AppEventConstants;
import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.app.ui.forms.ParametersModel;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;

public class EditDeviceHandler implements EventHandler {

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	private Object currentSelection;

	private Shell shell;
	
	private Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void init() {
		broker.subscribe(AppEventConstants.TOPIC_EDIT_DEVICE, this);
	}

	@Execute
	public void execute() throws IllegalArgumentException, IllegalAccessException {
		if (currentSelection != null && currentSelection instanceof Device) {			
			Device device = (Device) currentSelection;
			Device oldDevice = new Device(device);
			
			DeviceDialog dialog = new DeviceDialog(shell, oldDevice, new ParametersModel(oldDevice.getParams()));
			dialog.create();
			if (dialog.open() == Window.OK) {
				device=dialog.getDevice();
				Device d = dbService.findDeviceBySerialNum(Integer.parseInt(device.getSerialNum()));
				d.setDeviceName(device.getDeviceName());
				d.setParams(dialog.getParams());
				try {
					dbService.saveDevice(d);
					broker.post(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, d);
				} catch (Exception e) {
					MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

					ErrorDialog.openError(shell, "Ошибка", "Произошла ошибка", status);
					LOG.error(e.getMessage(), e);
				}
				
			}
			
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
	
	@Inject
	public void select(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Device device,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell){
		currentSelection = device;
		shell = parentShell;
	}

	@Override
	public void handleEvent(Event event) {
		try {
			execute();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object)
			throws IllegalArgumentException, IllegalAccessException {

		if (object instanceof Device)
			return true;
		return false;

	}
}