
package ru.sevenkt.app.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.AppEventConstants;
import ru.sevenkt.app.ui.forms.AddDeviceDialog;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;

public class AddDeviceHandler {
	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		Device device = new Device();
		AddDeviceDialog dialog = new AddDeviceDialog(parentShell);
		dialog.create();
		if (dialog.open() == Window.OK) {
			device.setDeviceName(dialog.getName());
			device.setSerialNum(dialog.getSerialNum()+"");
			try {
				dbService.saveDevice(device);
			} catch (Exception e) {
				MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
				ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
				LOG.error(e.getMessage(), e);
			}
			broker.post(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, device);
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