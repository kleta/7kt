package ru.sevenkt.reader.ui.handlers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ru.sevenkt.domain.IArchive;

public class SaveToFileJob extends Job{

	private List<IArchive> archives;
	private String path;

	public SaveToFileJob(List<IArchive> archives, String folderPath) {
		super("Сохранение в файл");
		this.archives=archives;
		path=folderPath;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Сохранение", archives.size());
		int i = 0;
		try {
			for (IArchive archive : archives) {
				
				monitor.subTask(archive.getName());
				archive.toFile(path);
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

}
