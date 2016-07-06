
package ru.sevenkt.app.ui.handlers;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.app.ui.forms.AssignReportDialog;
import ru.sevenkt.app.ui.forms.ReportPeriodDialog;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.reports.services.IReportService;

public class ShowReport {
	@Inject
	private IReportService reportService;
	@Inject
	private IDBService dbService;

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			@Named("ru.7kt.app.commandparameter.report") String param) {
		ReportPeriodDialog dialog = new ReportPeriodDialog(parentShell);
		int retOpen = dialog.open();
		if (retOpen == Window.OK) {
			Device device = (Device) object;
			Report report = dbService.findReport(Integer.parseInt(param));
			LocalDate dateFrom = dialog.getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate dateTo = dialog.getDateTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String reportType = dialog.getReportFormatt();
			Map<String, Object> reportParameters = new HashMap<>();

			List<Measuring> hourList = dbService.findArchive(device, dateFrom.atStartOfDay(), dateTo.atStartOfDay(),
					ArchiveTypes.HOUR);
			List<Measuring> dayList = dbService.findArchive(device, dateFrom.atStartOfDay(), dateTo.atStartOfDay(),
					ArchiveTypes.DAY);
			List<Measuring> monthList = dbService.findArchive(device, dateFrom.atStartOfDay(), dateTo.atStartOfDay(),
					ArchiveTypes.MONTH);
			List<Error> errors = dbService.findErrors(device, dateFrom.atStartOfDay(), dateTo.atStartOfDay(),
					ArchiveTypes.HOUR);
			errors.addAll(
					dbService.findErrors(device, dateFrom.atStartOfDay(), dateTo.atStartOfDay(), ArchiveTypes.DAY));
			errors.addAll(dbService.findErrors(device, dateFrom.atStartOfDay().minusMonths(1), dateTo.atStartOfDay(),
					ArchiveTypes.MONTH));
			List<Params> params = device.getParams();
			reportParameters.put(IReportService.DAY_MEASURINGS, dayList);
			reportParameters.put(IReportService.DEVICE, device);
			reportParameters.put(IReportService.HOUR_MEASURINGS, hourList);
			reportParameters.put(IReportService.MONTH_MEASURINGS, monthList);
			reportParameters.put(IReportService.PARAMS, params);
			reportParameters.put(IReportService.REPORT_TYPE, reportType);
			reportParameters.put(IReportService.ERRORS, errors);
			try {
				reportService.showReport(reportParameters, dateFrom, dateTo, report.getTemplateName(),
						report.getType());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}