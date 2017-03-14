
package ru.sevenkt.reader.ui.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.reader.services.IReaderService;

public class ReadData {
	
	@Inject 
	IReaderService service;
	
	
	
	@Execute
	public void execute(EPartService partService, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			EModelService modelService, MApplication application) {
		if (object != null) {
			if (object instanceof Device) {
				Device device = (Device) object;
				MPart part = (MPart) modelService.find("ru.7kt.reader.ui.partdescriptor.comlog", application);
				if (part == null) {
					MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
					part = partService.createPart("ru.7kt.reader.ui.partdescriptor.comlog");
					stack.getChildren().add(part);
				}
				partService.showPart(part, PartState.ACTIVATE);
				Thread thread=new Thread(){
					public void run(){
						try {
							service.readFullArchive(device.getConnection());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				thread.start();
			}
		}

	}

}