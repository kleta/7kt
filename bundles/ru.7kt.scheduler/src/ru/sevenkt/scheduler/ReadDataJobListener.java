package ru.sevenkt.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

public class ReadDataJobListener extends JobListenerSupport {

	@Override
	public String getName() {
		return "tlistener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub
		super.jobToBeExecuted(context);
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub
		super.jobExecutionVetoed(context);
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		// TODO Auto-generated method stub
		super.jobWasExecuted(context, jobException);
	}
	

}
