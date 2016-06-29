package ru.sevenkt.reports.services;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;

public interface IReportService {
	public String ERRORS = "errors";
	public String DAY_MEASURINGS = "d_measurings";
	public String HOUR_MEASURINGS = "h_measurings";
	public String MONTH_MEASURINGS = "m_measurings";
	public String PARAMS = "params";
	public String DEVICE = "device";
	public String REPORT_TYPE = "r_type";
	

	List<String> getAvailabelTemplates();

	void showReport(Map<String, Object> map, LocalDate dateFrom, LocalDate dateTo, String reportName,
			ArchiveTypes archiveType) throws FileNotFoundException;
}
