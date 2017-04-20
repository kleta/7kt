 
package ru.sevenkt.app.ui.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class OpenProgressViewHandler {
	@Execute
	public void execute(EPartService partService,
			EModelService modelService, MApplication application) {
		MPart part = (MPart) modelService.find("org.eclipse.e4.ui.progress.ProgressView",
				application);
		if (part == null) {
			MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
			part = partService.createPart("org.eclipse.e4.ui.progress.ProgressView");
			stack.getChildren().add(part);
		}
		partService.showPart(part, PartState.ACTIVATE);
	}
		
}