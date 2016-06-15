package ru.sevenkt.reports.services;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;

public interface IReportService {
	
	
	List<String> getAvailabelTemplates();

	void showReportAsHTML(List<Measuring> measurings, LocalDate dateFrom, LocalDate dateTo, String reportName,
			ArchiveTypes archiveType) throws FileNotFoundException;
}
