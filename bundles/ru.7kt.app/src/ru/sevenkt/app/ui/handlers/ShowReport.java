
package ru.sevenkt.app.ui.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;

import ru.sevenkt.reports.services.IReportService;

public class ShowReport {
	@Inject
	private IReportService reportService;

	@Execute
	public void execute() {

	}

}