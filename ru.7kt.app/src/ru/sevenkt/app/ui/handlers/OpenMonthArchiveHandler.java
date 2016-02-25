
package ru.sevenkt.app.ui.handlers;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import ru.sevenkt.db.entities.Device;

public class OpenMonthArchiveHandler {

	@Execute
	public void execute(EPartService partService, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object, EModelService modelService, MApplication application) {

		if (object != null) {
			if (object instanceof Device) {
				Device device = (Device)object;
				MPartStack stack = (MPartStack)modelService.find("ru.7kt.partstack.centr", application);
				MPart part = partService.createPart("ru.7kt.app.partdescriptor.arciveView");
				part.setLabel(device.getDeviceName()+" №"+device.getSerialNum());
				stack.getChildren().add(part);
				partService.showPart(part, PartState.ACTIVATE);
			}
		}
	}

}