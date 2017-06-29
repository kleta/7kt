
package ru.sevenkt.app.ui.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import ru.sevenkt.app.AppEventConstants;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.services.IDBService;

public class JournalSettingsHandler {
	
	@Inject
	private IDBService dbService;
	
	@Inject
	private IEventBroker broker;

	@Execute
	public void execute(EPartService partService, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			EModelService modelService, MApplication application) {
		if (object != null) {
			if (object instanceof Device) {
				Device device = (Device) object;
				MPart part = (MPart) modelService.find("ru.7kt.app.partdescriptor.journalsettings",
						application);
				if (part == null) {
					MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
					part = partService.createPart("ru.7kt.app.partdescriptor.journalsettings");
					stack.getChildren().add(part);
				}
				partService.showPart(part, PartState.ACTIVATE);
				List<Journal> list = dbService.findJournal(device);
				Map<String, Object> input=new HashMap<>();
				input.put(AppEventConstants.EVENTS, list);
				broker.send(AppEventConstants.TOPIC_SHOW_JOURNAL, input);
			}
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