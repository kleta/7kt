 
package ru.sevenkt.scheduler.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.quartz.SchedulerException;

import ru.sevenkt.scheduler.Group;
import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class RunHandler {
	@Inject 
	@Optional
	ISchedulerSevice schService;
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) Object object) throws SchedulerException  {
		Group group = (Group) object;
		schService.startJob(group);
	}
		
}