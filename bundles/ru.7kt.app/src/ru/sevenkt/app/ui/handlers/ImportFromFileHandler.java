
package ru.sevenkt.app.ui.handlers;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.progress.IProgressService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.container.Module.Settings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.ui.forms.DeviceDialog;
import ru.sevenkt.app.ui.forms.ParametersModel;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.Parameters;

public class ImportFromFileHandler {

	@Inject
	private IArchiveService archiveService;

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	@Inject
	@Optional
	IProgressService progressService;

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, IEclipseContext context,
			EPartService partService, EModelService modelService, MApplication application) throws Exception {
		FileDialog fd = new FileDialog(parentShell, SWT.MULTI);
		fd.setText("Открыть файл с данными");
		String[] filterExt = { "*.bin" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		LOG.info("Импорт архива " + selected);
		if (selected != null) {
			MPart part = (MPart) modelService.find("org.eclipse.e4.ui.progress.ProgressView", application);
			if (part == null) {
				MPartStack stack = (MPartStack) modelService.find("7kt.partstack.bottom", application);
				part = partService.createPart("org.eclipse.e4.ui.progress.ProgressView");
				stack.getChildren().add(part);
			}
			partService.showPart(part, PartState.ACTIVATE);
			String[] names = fd.getFileNames();
			int i = 0;
			for (String fname : names) {
				StringBuffer buf = new StringBuffer();
				buf.append(fd.getFilterPath());
				if (buf.charAt(buf.length() - 1) != File.separatorChar) {
					buf.append(File.separatorChar);
				}
				buf.append(fname);
				IArchive archive = archiveService.readArchiveFromFile(new File(buf.toString()));
				ImportArchiveJob job = ContextInjectionFactory.make(ImportArchiveJob.class, context);
				job.setArchive(archive);
				job.setRule(new ImportJobRule(i++));
				job.schedule();
				job.addJobChangeListener(new JobChangeAdapter() {
					public void done(IJobChangeEvent event) {
						if (event.getResult().isOK()) {
							LOG.info("Импорт " + archive.getName() + " успешно завершён");
						} else
							LOG.error("Импорт " + archive.getName() + " завершён c ошибками");
					}
				});
			}

		}
	}

}