
package ru.sevenkt.app.ui.handlers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.sevenkt.app.ui.TableRow;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.Parameters;

public class ExportToExcelHandler implements EventHandler {

	@Inject
	private IEventBroker broker;
	private Shell parentShell;

	Logger LOG = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void lazyLoadRequest(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		broker.subscribe(AppEventConstants.TOPIC_EXPORT_EXCEL, this);
		this.parentShell = parentShell;
	}

	@Override
	public void handleEvent(Event event) {
		Device device = (Device) event.getProperty(AppEventConstants.DEVICE);
		List<TableRow> tableRows = (List<TableRow>) event.getProperty(AppEventConstants.TABLE_ROWS);
		String tmpDir = System.getProperty("java.io.tmpdir");
		String fileName = tmpDir + device.getDeviceName()
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHss")) + ".xls";
		Workbook wb = new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy hh:mm"));
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(fileName);
			Sheet sheet = wb.createSheet(device.getDeviceName());
			TableRow firstRow = tableRows.get(0);
			Row row = sheet.createRow(0);
			row.createCell(0).setCellValue("Дата");
			Set<Parameters> parameters = firstRow.getValues().keySet();
			List<Parameters> params = parameters.stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName()))
					.collect(Collectors.toList());
			int i = 1;
			for (Parameters parameter : params) {
				row.createCell(i).setCellValue(parameter.getCategory() + " " + parameter.getName());
				i++;
			}
			i = 1;
			for (TableRow tr : tableRows) {
				row = sheet.createRow(i++);
				Instant instant = tr.getDateTime().atZone(ZoneId.systemDefault()).toInstant();
				Date date = Date.from(instant);
				Cell cell = row.createCell(0);
				cell.setCellValue(date);
				cell.setCellStyle(cellStyle);
				int k = 1;
				for (Parameters parameter : params) {
					Float val = (Float) tr.getValues().get(parameter);
					if (val != null)
						row.createCell(k).setCellValue(val);
					else
						row.createCell(k).setCellValue("Нет данных");
					k++;
				}

			}
			wb.write(fileOut);
			fileOut.close();
			String command = "cmd /c \"\"" + fileName + "\"\"";
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			MultiStatus status = createMultiStatus(e.getLocalizedMessage(), e);
			ErrorDialog.openError(parentShell, "Ошибка", "Произошла ошибка", status);
			LOG.error(e.getMessage(), e);
		}

	}

	private static MultiStatus createMultiStatus(String msg, Throwable t) {

		List<Status> childStatuses = new ArrayList<>();
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		for (StackTraceElement stackTrace : stackTraces) {
			Status status = new Status(IStatus.ERROR, "com.example.e4.rcp.todo", stackTrace.toString());
			childStatuses.add(status);
		}

		MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo", IStatus.ERROR,
				childStatuses.toArray(new Status[] {}), t.toString(), t);
		return ms;
	}
}