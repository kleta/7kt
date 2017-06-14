
package ru.sevenkt.reader.ui.handlers;

import java.time.LocalDate;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.app.ui.handlers.ImportArchiveJob;
import ru.sevenkt.app.ui.handlers.ImportJobRule;
import ru.sevenkt.db.entities.Connection;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.reader.services.IDeviceReaderService;
import ru.sevenkt.reader.ui.views.ReaderDialog;

public class ReadData {

	@Inject
	IDeviceReaderService service;

	@Inject
	private IEclipseContext context;

	@Execute
	public void execute(EPartService partService, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			EModelService modelService, MApplication application, Shell shell) {
		if (object != null) {
			if (object instanceof Device) {
				Device device = (Device) object;
				Connection connection = device.getConnection();
				if (connection == null) {
					MessageDialog.openError(shell, "Ошибка", "Не настроены параметры соединения с устройством.");
					return;
				}
				ReaderDialog dialog = new ReaderDialog(shell);
				if (dialog.open() == Window.OK) {

					MPart part = (MPart) modelService.find("ru.7kt.reader.ui.partdescriptor.comlog", application);
					if (part == null) {
						MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
						part = partService.createPart("ru.7kt.reader.ui.partdescriptor.comlog");
						stack.getChildren().add(part);
					}
					partService.showPart(part, PartState.ACTIVATE);

					Thread thread = new Thread() {
						public void run() {
							try {
								IArchive ar = null;

								if (dialog.isFull()) {
									ar = service.readFullArchive(connection);
								} else {
									Set<ArchiveTypes> types = dialog.getTypes();
									LocalDate from = dialog.getFrom();
									LocalDate to = dialog.getTo();
									boolean isJournal = dialog.isJournal();
									ar = service.readArchive(from, to, connection, types, isJournal);
								}
								if (ar != null) {
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											MPart part1 = (MPart) modelService
													.find("org.eclipse.e4.ui.progress.ProgressView", application);
											if (part1 == null) {
												MPartStack stack = (MPartStack) modelService
														.find("7kt.partstack.bottom", application);
												part1 = partService
														.createPart("org.eclipse.e4.ui.progress.ProgressView");
												stack.getChildren().add(part1);
											}
											partService.showPart(part1, PartState.ACTIVATE);
										}
									});
									if(dialog.getFolderPath()!=null)
										ar.toFile(dialog.getFolderPath());
									ImportArchiveJob job = ContextInjectionFactory.make(ImportArchiveJob.class,
											context);

									job.setArchive(ar);
									String name = ar.getName();
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
								} else {
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											MessageDialog.openError(shell, "Ошибка",
													"Не удалось считать архив. Проверьте настройки соединения с устройством");
										}
									});
								}

							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					thread.start();
				}
			}
		}

	}

}