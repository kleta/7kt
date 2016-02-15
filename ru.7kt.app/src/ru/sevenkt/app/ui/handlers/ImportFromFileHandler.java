
package ru.sevenkt.app.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.archive.domain.Archive;
import ru.sevenkt.archive.domain.Settings;
import ru.sevenkt.archive.services.IArchiveService;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;

public class ImportFromFileHandler {

	@Inject
	IArchiveService archiveService;

	@Inject
	IDBService dbService;

	Shell parentShell;

	Logger LOG = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		this.parentShell=parentShell;
		FileDialog fd = new FileDialog(parentShell, SWT.OPEN);
		fd.setText("Открыть файл с данными");
		String[] filterExt = { "*.bin" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		try {
			Archive archive = archiveService.readArchiveFromFile(new File(selected));
			insertOrUpdateDeviceSettings(archive.getSettings());
		} catch (Exception e) {

			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);

			ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
			LOG.error(e.getMessage(), e);
		}

	}

	private void insertOrUpdateDeviceSettings(Settings settings) {
		Device device = dbService.findDeviceBySerialNum(settings.getSerialNumber());
		if (device == null) {
			MessageBox dialog = new MessageBox(parentShell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			dialog.setText("Новое устройство");
			dialog.setMessage(
					"Устройство с номером " + settings.getSerialNumber() + " отсутствует в БД. Хотите добавить?");
			dialog.open();
		}
	}

	private static MultiStatus createMultiStatus(String msg, Throwable t) {

		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, "com.example.e4.rcp.todo", stackTrace.toString());
			childStatuses.add(status);
		}

		MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo", IStatus.ERROR,
				childStatuses.toArray(new Status[] {}), t.toString(), t);
		return ms;
	}

}