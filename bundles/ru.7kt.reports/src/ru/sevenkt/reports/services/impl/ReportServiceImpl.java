package ru.sevenkt.reports.services.impl;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.reports.Helper;
import ru.sevenkt.reports.services.IReportService;

public class ReportServiceImpl implements IReportService {

	@Override
	public List<String> getAvailabelTemplates() {
		List<String> templates = new ArrayList<String>();
		return templates;
	}

	@Override
	public void showReportAsHTML(List<Measuring> measurings, LocalDate dateFrom, LocalDate dateTo, String reportName, ArchiveTypes archiveType)
			throws FileNotFoundException {
		InputStream is = new FileInputStream(new File("templates/" + reportName));
		HashMap datasets = new HashMap();
		datasets.put("APP_CONTEXT_KEY_DEVICE", Helper.mapToDeviceData(measurings, dateFrom, dateTo, archiveType));
		datasets.put("APP_CONTEXT_KEY_CONSUMPTION", Helper.mapToConsumption(measurings, dateFrom, dateTo, archiveType));
		datasets.put("APP_CONTEXT_KEY_METERS", Helper.mapToMeters(measurings, dateFrom, dateTo, archiveType));

		// start enginee
		EngineConfig config = new EngineConfig();
		config.setEngineHome(System.getProperty("java.io.tmpdir"));
		config.setLogConfig("logs", Level.FINEST);
		try {
			Platform.startup(config);
			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			IReportEngine engine = factory.createReportEngine(config);
			engine.changeLogLevel(Level.WARNING);
			IReportRunnable runnable = engine.openReportDesign(is);

			// execute report
			// logger.debug("Generating report ...");
			RenderOption renderOption = null;
			renderOption = new HTMLRenderOption();
			renderOption.setOutputFormat("html");
			renderOption.setOutputFileName("templates/test.html");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			renderOption.setOutputStream(out);

			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			task.setAppContext(datasets);
			task.setRenderOption(renderOption);
			task.run();
			File htmlFile = new File("templates/test.html");
			Desktop.getDesktop().browse(htmlFile.toURI());
		} catch (BirtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// load report
		// logger.debug(String.format("Loading report %s...", name));
		
		// return new ByteArrayInputStream(out.toByteArray());
	}

}
