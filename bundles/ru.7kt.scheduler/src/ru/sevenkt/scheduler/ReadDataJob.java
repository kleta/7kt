package ru.sevenkt.scheduler;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Display;
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
		IEclipseContext eclipseContext=(IEclipseContext) data.get("eclipseContext");
		activatePortMonitor(eclipseContext);
		try {
			reader.addGroup(group);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void activatePortMonitor(IEclipseContext eclipseContext) {
		EPartService partService=eclipseContext.get(EPartService.class);
		EModelService modelService = eclipseContext.get(EModelService.class);
		MApplication application = eclipseContext.get(MApplication.class);
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MPart part = (MPart) modelService.find("ru.7kt.reader.ui.partdescriptor.comlog", application);
				if (part == null) {
					MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
					part = partService.createPart("ru.7kt.reader.ui.partdescriptor.comlog");
					stack.getChildren().add(part);
				}
				partService.showPart(part, PartState.ACTIVATE);		
			}
		});
		
	}

}
