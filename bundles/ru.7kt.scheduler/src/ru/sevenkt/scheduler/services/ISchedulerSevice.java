package ru.sevenkt.scheduler.services;

import ru.sevenkt.scheduler.Group;

public interface ISchedulerSevice {
//	void start() throws SchedulerException;
//	void stop() throws SchedulerException;
//	void restart() throws SchedulerException;
	void startJob(Group group);
}
