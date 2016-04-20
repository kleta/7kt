package ru.sevenkt.reports.services;

import java.time.LocalDate;
import java.util.List;

public interface IReportService {
	
	void showReport(String connectionString, String templateName, Integer idDevice, LocalDate dateFrom,
			LocalDate dateTo);

	List<String> getAvailabelTemplates();
}
