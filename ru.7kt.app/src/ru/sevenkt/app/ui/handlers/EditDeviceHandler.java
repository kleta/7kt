
package ru.sevenkt.app.ui.handlers;

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

import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;

public class EditDeviceHandler implements EventHandler {

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	private Object currentSelection;

	private Shell shell;

	@PostConstruct
	public void init() {
		broker.subscribe(AppEventConstants.TOPIC_EDIT_DEVICE, this);
	}

	@Execute
	public void execute() {
		if (currentSelection != null && currentSelection instanceof Device) {
			
			
			Device device = (Device) currentSelection;
			Device oldDevice = new Device(device);
			
			DeviceDialog dialog = new DeviceDialog(shell, oldDevice);
			dialog.create();
			if (dialog.open() == Window.OK) {
				dbService.saveDevice(dialog.getDevice());
				broker.send(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, device);
			}
			
		}
	}
	@Inject
	public void select(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Device device,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell){
		currentSelection = device;
		shell = parentShell;
	}

	@Override
	public void handleEvent(Event event) {
		execute();
	}

}