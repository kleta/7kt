package ru.sevenkt.scheduler;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.scheduler.services.ISchedulerSevice;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.CalendarIntervalScheduleBuilder.*;
import static org.quartz.JobKey.*;
import static org.quartz.TriggerKey.*;
import static org.quartz.DateBuilder.*;
import static org.quartz.impl.matchers.KeyMatcher.*;
import static org.quartz.impl.matchers.GroupMatcher.*;
import static org.quartz.impl.matchers.AndMatcher.*;
import static org.quartz.impl.matchers.OrMatcher.*;
import static org.quartz.impl.matchers.EverythingMatcher.*;

public class SchedulerServiceImpl implements ISchedulerSevice {

	@Inject
	private IDBService dbs;

	@Inject
	private Reader reader;

	private Scheduler scheduler;

	@PostConstruct
	void init() throws SchedulerException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.getListenerManager().addJobListener(new ReadDataJobListener());
	}

	@Override
	public void start() throws SchedulerException {
		scheduler.start();
		createJobs();
	}

	private void createJobs() throws SchedulerException {
		List<SchedulerGroup> sgs = dbs.findAllShedulerGroup();
		for (SchedulerGroup schedulerGroup : sgs) {
			if (schedulerGroup.isEnabled()) {
				Group group = new Group(schedulerGroup);
				JobDataMap data = new JobDataMap();
				data.put("group", group);
				data.put("reader", reader);
				JobDetail job = newJob(ReadDataJob.class).withIdentity("job" + group.getSchedulerGroup().getId(),
						"group" + group.getSchedulerGroup().getId()).setJobData(data).build();
				String cronString = group.getSchedulerGroup().getCronString();
				Trigger trigger = newTrigger()
						.withIdentity("trigger" + group.getSchedulerGroup().getId(),
								"group" + group.getSchedulerGroup().getId())
						.withSchedule(cronSchedule(cronString)).build();
				scheduler.scheduleJob(job, trigger);
			}
		}
	}

	@Override
	public void stop() throws SchedulerException {
		scheduler.shutdown();
	}

	@Override
	public void restart() throws SchedulerException {
		scheduler.clear();
		createJobs();
	}

	@Override
	public void startJob(Group group) throws SchedulerException {
		scheduler.clear();
		JobDataMap data = new JobDataMap();
		data.put("group", group);
		data.put("reader", reader);
		JobDetail job = newJob(ReadDataJob.class).withIdentity("jobRunOnce" + group.getSchedulerGroup().getId(),
				"groupRunOnce" + group.getSchedulerGroup().getId()).setJobData(data).build();
		Trigger runOnceTrigger = newTrigger().build();
		scheduler.scheduleJob(job, runOnceTrigger);
		createJobs();
	}

}
