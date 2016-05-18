
package ru.sevenkt.app.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisTick;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import ru.sevenkt.app.ui.handlers.AppEventConstants;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class ArchiveView implements EventHandler {
	@Inject
	private IEventBroker broker;

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Table table;

	private Device device;

	private Date startPeriodSelectedDate;

	private Date endPeriodSelectedDate;

	private Button requestButton;

	private ArchiveTypes selectedArchiveType;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	private TableViewer tableViewer;
	private ScrolledForm сhartsScrolledform;

	private IAction exportToExcelAction;

	private List<Parameters> parameters;

	@Inject
	public ArchiveView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Device device) {
		this.device = device;
		createActions();
		broker.subscribe(AppEventConstants.TOPIC_RESPONSE_ARCHIVE, this);
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER | SWT.BOTTOM);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Таблица");

		tabFolder.setSelection(0);

		ScrolledForm arсhiveScrolledform = formToolkit.createScrolledForm(tabFolder);
		tbtmNewItem_1.setControl(arсhiveScrolledform);
		formToolkit.paintBordersFor(arсhiveScrolledform);
		arсhiveScrolledform.setText("Просмотр архива");
		arсhiveScrolledform.getBody().setLayout(new FormLayout());
		ToolBarManager tbm = (ToolBarManager) arсhiveScrolledform.getToolBarManager();
		tbm.add(exportToExcelAction);
		tbm.update(true);
		arсhiveScrolledform.reflow(true);

		Section sctnNewSection = formToolkit.createSection(arсhiveScrolledform.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sctnNewSection = new FormData();
		fd_sctnNewSection.right = new FormAttachment(100, -4);
		fd_sctnNewSection.left = new FormAttachment(0);
		fd_sctnNewSection.bottom = new FormAttachment(0, 67);
		fd_sctnNewSection.top = new FormAttachment(0);
		sctnNewSection.setLayoutData(fd_sctnNewSection);
		formToolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Период просмотра");
		sctnNewSection.setExpanded(true);

		Composite composite = formToolkit.createComposite(sctnNewSection, SWT.NONE);
		formToolkit.paintBordersFor(composite);
		sctnNewSection.setClient(composite);
		composite.setLayout(new FormLayout());

		Label label = formToolkit.createLabel(composite, "Начало периода", SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 10);
		fd_label.left = new FormAttachment(0);
		label.setLayoutData(fd_label);

		CDateTime startPeriodDateTime = new CDateTime(composite, CDT.BORDER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		startPeriodDateTime.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				startPeriodSelectedDate = startPeriodDateTime.getSelection();
				if (endPeriodSelectedDate != null && endPeriodSelectedDate.after(startPeriodSelectedDate)) {
					requestButton.setEnabled(true);
				} else
					requestButton.setEnabled(false);
			}
		});
		FormData fd_startPeriodDateTime = new FormData();
		fd_startPeriodDateTime.left = new FormAttachment(label, 6);
		fd_startPeriodDateTime.top = new FormAttachment(0, 2);
		startPeriodDateTime.setLayoutData(fd_startPeriodDateTime);
		formToolkit.adapt(startPeriodDateTime);
		formToolkit.paintBordersFor(startPeriodDateTime);

		Label label_1 = formToolkit.createLabel(composite, "Окончание периода", SWT.NONE);
		fd_startPeriodDateTime.right = new FormAttachment(label_1, -53);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 0, SWT.TOP);
		label_1.setLayoutData(fd_label_1);

		CDateTime endPeriodDateTime = new CDateTime(composite,
				CDT.BORDER | CDT.SPINNER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		endPeriodDateTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				endPeriodSelectedDate = endPeriodDateTime.getSelection();
				if (startPeriodSelectedDate != null && endPeriodSelectedDate.after(startPeriodSelectedDate)) {
					requestButton.setEnabled(true);
				} else
					requestButton.setEnabled(false);
			}
		});
		fd_label_1.right = new FormAttachment(100, -545);
		FormData fd_endPeriodDateTime = new FormData();
		fd_endPeriodDateTime.right = new FormAttachment(label_1, 136, SWT.RIGHT);
		fd_endPeriodDateTime.top = new FormAttachment(0, 2);
		fd_endPeriodDateTime.left = new FormAttachment(label_1, 6);
		endPeriodDateTime.setLayoutData(fd_endPeriodDateTime);
		formToolkit.adapt(endPeriodDateTime);
		formToolkit.paintBordersFor(endPeriodDateTime);

		ComboViewer comboViewer = new ComboViewer(composite, SWT.NONE);
		comboViewer.addSelectionChangedListener(listener -> {
			IStructuredSelection selection = comboViewer.getStructuredSelection();
			selectedArchiveType = (ArchiveTypes) selection.getFirstElement();
		});
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setInput(ArchiveTypes.values());
		ISelection selection = new StructuredSelection(ArchiveTypes.values()[0]);
		comboViewer.setSelection(selection);
		Combo combo = comboViewer.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(startPeriodDateTime, 0, SWT.TOP);
		fd_combo.right = new FormAttachment(endPeriodDateTime, 199, SWT.RIGHT);
		fd_combo.left = new FormAttachment(endPeriodDateTime, 6);
		combo.setLayoutData(fd_combo);
		formToolkit.paintBordersFor(combo);

		requestButton = formToolkit.createButton(composite, "Запросить", SWT.NONE);
		requestButton.setEnabled(false);
		requestButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				LocalDate startDate = startPeriodSelectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate endDate = endPeriodSelectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				map.put(AppEventConstants.DEVICE, device);
				map.put(AppEventConstants.START_DATE, startDate);
				map.put(AppEventConstants.END_DATE, endDate);
				map.put(AppEventConstants.ARCHIVE_TYPE, selectedArchiveType);
				Table table = tableViewer.getTable();
				Composite parent = table.getParent();
				table.dispose();
				tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
				tableViewer.setContentProvider(ArrayContentProvider.getInstance());
				parent.layout(true);
				broker.send(AppEventConstants.TOPIC_REQUEST_ARCHIVE, map);
			}
		});
		FormData fd_requestButton = new FormData();
		fd_requestButton.bottom = new FormAttachment(label, 0, SWT.BOTTOM);
		fd_requestButton.left = new FormAttachment(combo, 6);
		combo.select(0);
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				ArchiveTypes type = (ArchiveTypes) element;
				return type.getName();
			}
		});
		requestButton.setLayoutData(fd_requestButton);

		Section sctnNewSection_1 = formToolkit.createSection(arсhiveScrolledform.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		FormData fd_sctnNewSection_1 = new FormData();
		fd_sctnNewSection_1.right = new FormAttachment(sctnNewSection, 0, SWT.RIGHT);
		fd_sctnNewSection_1.top = new FormAttachment(sctnNewSection, 1);
		fd_sctnNewSection_1.bottom = new FormAttachment(100);
		fd_sctnNewSection_1.left = new FormAttachment(0);
		sctnNewSection_1.setLayoutData(fd_sctnNewSection_1);
		formToolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText("Значения");
		sctnNewSection_1.setExpanded(true);

		Composite composite_1 = formToolkit.createComposite(sctnNewSection_1, SWT.NONE);
		formToolkit.paintBordersFor(composite_1);
		sctnNewSection_1.setClient(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		formToolkit.paintBordersFor(table);

		CTabItem tbtmChartsItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmChartsItem.setText("Графики");

		сhartsScrolledform = formToolkit.createScrolledForm(tabFolder);
		tbtmChartsItem.setControl(сhartsScrolledform);
		formToolkit.paintBordersFor(сhartsScrolledform);
		сhartsScrolledform.setText("Графики");
		сhartsScrolledform.getBody().setLayout(new FillLayout(SWT.VERTICAL));

	}

	@Override
	public void handleEvent(Event event) {
		Object obj = event.getProperty(AppEventConstants.DEVICE);
		if (device.equals(obj)) {
			parameters = (List<Parameters>) event.getProperty(AppEventConstants.ARCHIVE_PARAMETERS);
			table = tableViewer.getTable();
			table.setLinesVisible(true);
			table.setHeaderVisible(true);
			TableViewerColumn dateViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			dateViewerColumn.setLabelProvider(new ColumnLabelProvider() {
				public Image getImage(Object element) {
					// TODO Auto-generated method stub
					return null;
				}

				public String getText(Object element) {
					LocalDateTime date;
					if (((TableRow) element).getDateTime() instanceof LocalDateTime) {
						date = (LocalDateTime) ((TableRow) element).getDateTime();
						return element == null ? "" : date.format(formatter);
					} else
						return element == null ? "" : ((TableRow) element).getDateTime().toString();
				}
			});
			TableColumn dateColumn = dateViewerColumn.getColumn();
			// dateColumn.setWidth(100);
			dateColumn.setText("Дата");
			if (!parameters.isEmpty()) {
				parameters.sort((p1, p2) -> new Integer(p1.getOrderIndex()).compareTo(new Integer(p2.getOrderIndex())));
				for (Parameters parameter : parameters) {
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					tableViewerColumn.setLabelProvider(new ArchiveColumnLabelProvider(parameter, selectedArchiveType));
					TableColumn column = tableViewerColumn.getColumn();
					// column.setWidth(100);
					column.setText(parameter.getName());
				}

			}
			List<?> input = (List<?>) event.getProperty(AppEventConstants.TABLE_ROWS);
			tableViewer.setInput(input);
			TableColumn[] columns = tableViewer.getTable().getColumns();
			for (TableColumn tableColumn : columns) {
				tableColumn.pack();
			}
			exportToExcelAction.setEnabled(true);
			createCharts(input);

		}
	}

	private void createCharts(List<?> input) {
		removeCharts();
		Map<String, List<Parameters>> groupByCategory = parameters.stream()
				.collect(Collectors.groupingBy(Parameters::getCategory));
		for (String key : groupByCategory.keySet()) {
			if (key.equals(ParametersConst.ENERGY) || key.equals(ParametersConst.VOLUME)
					|| key.equals(ParametersConst.WEIGHT) || key.equals(ParametersConst.TEMP)
					|| key.equals(ParametersConst.PRESSURE)) {
				// // Label l=new Label(сhartsScrolledform.getBody(), SWT.NONE);
				// // l.setText(key);
				Section section = formToolkit.createSection(сhartsScrolledform.getBody(),
						Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
				formToolkit.paintBordersFor(section);
				section.setText(key);

				Composite composite = formToolkit.createComposite(section, SWT.NONE);
				formToolkit.paintBordersFor(composite);
				composite.setLayout(new FillLayout(SWT.VERTICAL));
				section.setClient(composite);
				Chart chart = new Chart(composite, SWT.NONE);
				chart.getTitle().setVisible(false);
				IAxis xAxis = chart.getAxisSet().getXAxis(0);

				xAxis.getTitle().setVisible(false);
				chart.getAxisSet().getYAxis(0).getTitle().setText(groupByCategory.get(key).get(0).getUnit());
				List<Parameters> params = groupByCategory.get(key);
				createDateFormat(chart);
				int i = 3;
				for (Parameters parameter : params) {
					Color c = Display.getDefault().getSystemColor(i);
					createSeries(parameter, input, chart, c);
					i = i + 3;
				}
				chart.getAxisSet().adjustRange();
				сhartsScrolledform.getBody().layout(true);
			}
		}

	}

	private void createDateFormat(Chart chart) {
		DateFormat format;
		switch (selectedArchiveType) {
		case MONTH:
			format = new SimpleDateFormat("MM.YY");
			break;
		case DAY:
			format = new SimpleDateFormat("dd.MM.yy");
			break;
		case HOUR:
			format = new SimpleDateFormat("dd.MM.yy HH:mm");
			break;

		default:
			format = new SimpleDateFormat("dd.MM.yy HH:mm");
			break;
		}
		IAxisTick xTick = chart.getAxisSet().getXAxis(0).getTick();
		xTick.setFormat(format);

	}

	private void createSeries(Parameters parameter, List<?> input, Chart chart, Color c) {
		Date[] xSeries = new Date[input.size()];
		double[] ySeries = new double[input.size()];
		int i = 0;
		for (Object object : input) {
			TableRow tr = (TableRow) object;
			if (tr.getDateTime() instanceof LocalDateTime) {
				LocalDateTime ldt = (LocalDateTime) tr.getDateTime();
				Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();

				Double val = (Double) tr.getValues().get(parameter);
				xSeries[i] = Date.from(instant);
				if (val == null) {
					ySeries[i++] = -0.1;
				} else
					ySeries[i++] = val;
			}
		}
		ILineSeries series = (ILineSeries) chart.getSeriesSet().createSeries(SeriesType.LINE, parameter.getName());
		series.setXDateSeries(xSeries);
		series.setLineColor(c);
		series.setYSeries(ySeries);
		series.setSymbolType(PlotSymbolType.NONE);
	}

	private void removeCharts() {
		Control[] charts = сhartsScrolledform.getBody().getChildren();
		for (Control control : charts) {
			control.dispose();
		}
		сhartsScrolledform.getBody().setLayout(new FillLayout(SWT.VERTICAL));
	}

	private void createActions() {
		exportToExcelAction = new Action("Экспорт в Excel") {

			@Override
			public void run() {
				List<TableRow> tableRows = (List<TableRow>) tableViewer.getInput();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(AppEventConstants.DEVICE, device);
				map.put(AppEventConstants.TABLE_ROWS, tableRows);
				map.put(AppEventConstants.ARCHIVE_PARAMETERS, parameters);
				map.put(AppEventConstants.ARCHIVE_TYPE, selectedArchiveType);
				broker.send(AppEventConstants.TOPIC_EXPORT_EXCEL, map);
			}

		};
		exportToExcelAction.setImageDescriptor(
				ResourceManager.getPluginImageDescriptor("ru.7kt.app", "icons/application-vnd.ms-excel.ico"));
		exportToExcelAction.setEnabled(false);
	}
}