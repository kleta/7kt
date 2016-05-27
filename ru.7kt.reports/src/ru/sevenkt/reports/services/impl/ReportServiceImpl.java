package ru.sevenkt.reports.services.impl;

import java.time.LocalDate;
import java.util.List;

import ru.sevenkt.reports.services.IReportService;

public class ReportServiceImpl implements IReportService {

	@Override
	public void showReport(String connectionString, String templateName, Integer idDevice, LocalDate dateFrom,
			LocalDate dateTo) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getAvailabelTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

}
