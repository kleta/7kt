 
package ru.sevenkt.scheduler.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class RunHandler {
	@Inject 
	@Optional
	ISchedulerSevice schService;
	
	@Execute
	public void execute() {
		
	}
		
}