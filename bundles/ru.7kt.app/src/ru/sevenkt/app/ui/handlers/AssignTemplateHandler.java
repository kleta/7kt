 
package ru.sevenkt.app.ui.handlers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.ui.forms.AssignReportDialog;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.reports.services.IReportService;

public class AssignTemplateHandler {
	
	@Inject
	private IReportService rs;
	
	@Inject
	private IDBService dbs;
	
	private Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		List<String> templates = rs.getAvailabelTemplates();
		Device device=(Device)object;
		List<Report> reports = dbs.getReports(device);
		AssignReportDialog dialog=new AssignReportDialog(parentShell, templates, reports);
		int retOpen = dialog.open();
		if (retOpen == Window.OK) {
			System.out.println();
		}
		
	}
		
}