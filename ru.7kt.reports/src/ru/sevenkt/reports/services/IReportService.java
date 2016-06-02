package ru.sevenkt.reports.services;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import ru.sevenkt.db.entities.Measuring;

public interface IReportService {
	
	void showReportAsHTML(List<Measuring> measurings,LocalDate dateFrom,
			LocalDate dateTo, String reportName) throws FileNotFoundException;

	List<String> getAvailabelTemplates();
}
