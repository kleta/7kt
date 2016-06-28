
package ru.sevenkt.app.ui.handlers;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
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
	public void execute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object, @Named("ru.7kt.app.commandparameter.report") String param) {
		Device device = (Device) object;
		Report report=dbService.findReport(Integer.parseInt(param));		
		LocalDate startDate = LocalDate.parse("20160101", DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDate=LocalDate.parse("20160217", DateTimeFormatter.ofPattern("yyyyMMdd"));;
		List<Measuring> m = dbService.findArchive(device, startDate.atStartOfDay(), endDate.atStartOfDay(), report.getType());		
		try {
			reportService.showReportAsHTML(m, startDate, endDate, report.getTemplateName(), report.getType());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}