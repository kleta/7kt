package ru.sevenkt.reader.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ru.sevenkt.domain.IArchive;
import ru.sevenkt.reader.services.Archive7KTC;
import ru.sevenkt.reader.services.IReaderService;

public class ReadArchivesJob extends Job {

	private List<Object> archives;
	private IReaderService reader;
	private String port;
	private List<IArchive> archivesList;

	public ReadArchivesJob(List<Object> archives, IReaderService reader, String port) {
		super(port);
		this.port = port;
		this.archives = archives;
		this.reader = reader;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Чтение", archives.size());
		int i = 0;
		archivesList=new ArrayList<>();
		try {
			for (Object object : archives) {
				Archive7KTC archive = (Archive7KTC) object;
				monitor.subTask(archive.getName());
				IArchive arhive = reader.readArchive(archive, port);
				archivesList.add(arhive);
				monitor.worked(i++);
			}

		} catch (Exception e) {
			MultiStatus result = new MultiStatus("ru.7kt.reader", IStatus.ERROR, e.getMessage(), e);
			StackTraceElement[] elements = e.getStackTrace();
			for (StackTraceElement stackTraceElement : elements) {
				result.addAll(new Status(IStatus.ERROR, "ru.7kt.reader", stackTraceElement.toString()));

			}
			return result;
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}

	public List<IArchive> getArchivesList() {
		return archivesList;
	}

}
