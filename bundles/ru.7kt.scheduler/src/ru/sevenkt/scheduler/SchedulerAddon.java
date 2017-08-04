
package ru.sevenkt.scheduler;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.quartz.SchedulerException;

import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class SchedulerAddon {

	public SchedulerAddon() {
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void applicationStarted(IEclipseContext context) throws SchedulerException {
		SchedulerServiceImpl sch = ContextInjectionFactory.make(SchedulerServiceImpl.class, context);
		context.set(ISchedulerSevice.class, sch);
		sch.start();
	}

}
