
package ru.sevenkt.reader.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.app.ui.handlers.ImportArchiveJob;
import ru.sevenkt.app.ui.handlers.ImportJobRule;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.reader.services.Archive7KTC;
import ru.sevenkt.reader.services.IReaderService;
import ru.sevenkt.reader.ui.views.ImportFrom7KTCDialog;

public class Read7ktcHandler {

	@Inject
	private IReaderService reader;

	@Inject
	private IDBService dbService;

	@Inject
	private IEventBroker broker;

	@Inject
	private IEclipseContext context;

	@Execute
	public void execute(Shell shell, EPartService partService,
			EModelService modelService, MApplication application) {
		ImportFrom7KTCDialog dialog = new ImportFrom7KTCDialog(shell, this);
		if (dialog.open() == Window.OK) {
			MPart part = (MPart) modelService.find("org.eclipse.e4.ui.progress.ProgressView",
					application);
			if (part == null) {
				MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
				part = partService.createPart("org.eclipse.e4.ui.progress.ProgressView");
				stack.getChildren().add(part);
			}
			partService.showPart(part, PartState.ACTIVATE);
			String port = dialog.getComPort();
			List<Object> archives = dialog.getSelectedElements();
			ReadArchivesJob job=new ReadArchivesJob(archives, reader, port);
			job.schedule();
			job.addJobChangeListener(new JobChangeAdapter() {
				public void done(IJobChangeEvent event) {
					if (event.getResult().isOK()){
						int i=0;
						for(IArchive archive:job.getArchivesList()){
							persistArhiveToDatabase(archive, i);
							i+=5000;
						}
						if(dialog.isSaveToFile()){
							String folder = dialog.getSaveFolder();
							SaveToFileJob saveJob = new SaveToFileJob(job.getArchivesList(), folder);
							saveJob.schedule();
						}
						
					}
					else
						System.out.println("Job did not complete successfully");
				}
			});

		}
	}

	private void persistArhiveToDatabase(IArchive archive, int i) {
		ImportArchiveJob job = ContextInjectionFactory.make(ImportArchiveJob.class, context);
		job.setArchive(archive);
		job.setRule(new ImportJobRule(i));
		job.schedule();
		job.addJobChangeListener(new JobChangeAdapter() {
			public void done(IJobChangeEvent event) {
				if (event.getResult().isOK()){
					System.out.println("Job completed successfully "+archive.getName());
				}
				else
					System.out.println("Job did not complete successfully");
			}
		});
	}

	public List<String> getPorts() {
		return new ArrayList<>(reader.getAvailableSerialPorts());
	}

	public int getArchiveCount(String port, boolean b) throws Exception {
		return reader.getArchiveCount(port, b);
	}


	public List<Archive7KTC> readArchives(String port, ImportFrom7KTCDialog dialog, boolean showDeleted) {
		try {
			int c = reader.getArchiveCount(port, showDeleted);
			dialog.setPgVisible(true);
			dialog.setMessage("");
			dialog.setErrorMessage(null);
			dialog.setProgressBarMaximum(c);
			int adr = 0;
			List<Archive7KTC> list = new ArrayList<>();
			for (int i = 0; i < c; i++) {
				Archive7KTC arch;
				arch = reader.readFirstPageArchive(port, adr);
				dialog.updateProgressBar("Найден архив: " + arch.getName());
				adr = arch.getFirstBlockNum() + arch.getPagesInBlock() * arch.getNumberOfBlock();
				list.add(arch);
			}
			Thread.sleep(500);
			dialog.setPgVisible(false);
			return list;

		} catch (Exception e) {
			dialog.setErrorMessage(e.getMessage());
		}
		return null;
	}

}