 
package ru.sevenkt.scheduler.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;

import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.scheduler.Group;
import ru.sevenkt.scheduler.SchedulerEventConstants;

public class DeleteGroupHandler {
	@Inject
	private IDBService dbService;
	
	@Inject
	private IEventBroker broker;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		SchedulerGroup schedulerGroup = ((Group) object).getSchedulerGroup();
		schedulerGroup.setArchiveTypes(null);
		dbService.deleteSchedulerGroup(schedulerGroup);
		broker.post(SchedulerEventConstants.TOPIC_REFRESH_SCHEDULER_VIEW, object);
	}
	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object)
			throws IllegalArgumentException, IllegalAccessException {
		if (object instanceof Group)
			return true;
		return false;

	}
		
}