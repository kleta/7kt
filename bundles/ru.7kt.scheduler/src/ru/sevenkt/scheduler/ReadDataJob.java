package ru.sevenkt.scheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReadDataJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getMergedJobDataMap();
		Group group = (Group)data.get("group");
		System.out.println("group = " + group);
		Reader reader = (Reader)data.get("reader");
		System.out.println("reader = " + reader);
		try {
			reader.addGroup(group);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
