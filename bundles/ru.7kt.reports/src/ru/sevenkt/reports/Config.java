package ru.sevenkt.reports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.sevenkt.reports.services.IReportService;
import ru.sevenkt.reports.services.impl.ReportServiceImpl;

@Configuration
public class Config {	
	@Bean
	public IReportService reportService(){
		return new ReportServiceImpl();
	}

	
}
