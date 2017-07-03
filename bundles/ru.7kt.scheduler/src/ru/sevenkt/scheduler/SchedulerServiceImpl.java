package ru.sevenkt.scheduler;

import ru.sevenkt.scheduler.services.ISchedulerSevice;

public class SchedulerServiceImpl implements ISchedulerSevice {

//	@Inject
//	private IDBService dbs;
//
//	private Scheduler scheduler;
//
//	@PostConstruct
//	void init() throws SchedulerException {
//		scheduler = StdSchedulerFactory.getDefaultScheduler();
//	}
//
//	@Override
//	public void start() throws SchedulerException {
//		List<SchedulerGroup> sgs = dbs.findAllShedulerGroup();
//		for (SchedulerGroup schedulerGroup : sgs) {
//			System.out.println(schedulerGroup);
//		}
//		scheduler.start();
//	}
//
//	@Override
//	public void stop() throws SchedulerException {
//		scheduler.shutdown();
//
//	}
//
//	@Override
//	public void restart() throws SchedulerException {
//		stop();
//		start();
//	}
//
	@Override
	public void startJob(Group group) {
		// TODO Auto-generated method stub

	}

}
