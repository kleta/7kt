
package ru.sevenkt.app.ui.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;

public class DeleteDevice {

	@Inject
	private IDBService db;

	@Inject
	private IEventBroker broker;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object, EPartService partService,
			EModelService modelService, MApplication application,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		if (object instanceof Device) {
			Device device = (Device) object;
			MessageDialog dialog = new MessageDialog(parentShell, "Удаление", null,
					"Будет удалено устройство <" + device.getDeviceName() + " №" + device.getSerialNum()
							+ "> и все данные связанные с ним. Продолжить?",
					MessageDialog.QUESTION, new String[] { "Да", "Нет" }, 1);
			int result = dialog.open();
			if (result == 0) {
				db.deleteDevice(device);
				LOG.info("Удалено устройство {} №{}", device.getDeviceName(), device.getSerialNum());
				broker.send(AppEventConstants.TOPIC_REFRESH_DEVICE_VIEW, device);
				closeArchiveView(device, partService, modelService, application);
			}
		}
	}

	private void closeArchiveView(Device device, EPartService partService, EModelService modelService,
			MApplication application) {

		MPart part = (MPart) modelService.find("ru.7kt.app.partdescriptor.arciveView_" + device.getId(), application);
		if (part != null) {
			partService.hidePart(part);
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