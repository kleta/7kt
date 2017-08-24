package ru.sevenkt.scheduler;

import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.ui.handlers.ImportArchiveJob;
import ru.sevenkt.app.ui.handlers.ImportJobRule;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.reader.services.IDeviceReaderService;

@Singleton
@Creatable
public class Reader {

	@Inject
	private IDeviceReaderService rs;

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService db;

	@Inject
	private IEclipseContext context;

	@Inject
	EPartService partService;
	@Inject
	EModelService modelService;
	@Inject
	MApplication application;

	private ArrayDeque<Group> groupQueue;

	private Set<String> ports;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void init() {
		ports = rs.getAvailableSerialPorts();
		groupQueue = new ArrayDeque<>();
	}

	public void addGroup(Group group) throws InterruptedException {
		groupQueue.addLast(group);
		while (!groupQueue.isEmpty()) {
			Group gr = groupQueue.pollFirst();
			LOG.info("Ваполняется группа " + gr.getName());
			broker.send(SchedulerEventConstants.TOPIC_GROUP_STATE_EXECUTE, group);
			if (gr != null) {
				readGroup(gr);
			}
			LOG.info("Завершено выполнение группы " + gr.getName());
			broker.send(SchedulerEventConstants.TOPIC_GROUP_STATE_WAIT, group);
		}
	}

	private void readGroup(Group group) {
		rs.initPorts();
		LocalDate fromDate = LocalDate.now().minusDays(group.getDeepDays());
		Collection<ArchiveTypes> archiveTypes = group.getArchiveTypes();
		List<Device> devices = group.getSchedulerGroup().getDevices();
		Map<String, ArrayDeque<DeviceWrap>> portsQueues;
		portsQueues = new HashMap<>();
		for (Device device : devices) {
			DeviceWrap dw = new DeviceWrap(group, device, archiveTypes, fromDate, 1);
			Connection connection = device.getConnection();
			if (connection != null && ports.contains(connection.getPort())) {
				String port = connection.getPort();
				ArrayDeque<DeviceWrap> queue = portsQueues.get(port);
				if (queue == null) {
					queue = new ArrayDeque<>();
					portsQueues.put(port, queue);
				}
				queue.addLast(dw);
			} else
				LOG.error("Не настроено соединение с устройством "+device.getDeviceName()+" №"+device.getSerialNum());
		}

		try {
			startRead(portsQueues);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		rs.closeAllPorts();
	}

	private void startRead(Map<String, ArrayDeque<DeviceWrap>> portsQueues) throws InterruptedException {
		Set<String> keys = portsQueues.keySet();
		ExecutorService es = Executors.newCachedThreadPool();
		for (String portName : keys) {
			es.execute(new Runnable() {
				public void run() {
					ArrayDeque<DeviceWrap> q = portsQueues.get(portName);
					if (q != null) {
						while (!q.isEmpty()) {
							DeviceWrap dw = q.pollFirst();
							if (dw != null) {
								try {
									IArchive arch = rs.readArchive(dw.getFromDate(), LocalDate.now(),
											dw.getDevice().getConnection(), new HashSet<>(dw.getTypes()), false);
									if (arch != null) {
										saveArchive(arch);
									} else {
										dw.setTryCount(dw.getTryCount() - 1);
										if (dw.getTryCount() > 0) {
											q.addLast(dw);
										} else {

											System.out.println("Истекли попытки считать данные с устройства: "
													+ dw.getDevice().getDeviceName());
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									dw.setTryCount(dw.getTryCount() - 1);
									if (dw.getTryCount() > 0) {
										q.addLast(dw);
									} else {

										System.out.println("Истекли попытки считать данные с устройства: "
												+ dw.getDevice().getDeviceName());
									}
								}
							}
						}
					}

				}
			});

		}
		es.shutdown();
		es.awaitTermination(30, TimeUnit.MINUTES);
	}

	protected void saveArchive(IArchive arch) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MPart part1 = (MPart) modelService.find("org.eclipse.e4.ui.progress.ProgressView", application);
				if (part1 == null) {
					MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
					part1 = partService.createPart("org.eclipse.e4.ui.progress.ProgressView");
					stack.getChildren().add(part1);
				}
				partService.showPart(part1, PartState.ACTIVATE);
			}
		});

		ImportArchiveJob job = ContextInjectionFactory.make(ImportArchiveJob.class, context);

		job.setArchive(arch);
		String name = arch.getName();
		job.setRule(new ImportJobRule(1));
		job.schedule();
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()) {
					System.out.println("Job completed successfully " + name);
				} else
					System.out.println("Job did not complete successfully");
			}
		});

	}
}
