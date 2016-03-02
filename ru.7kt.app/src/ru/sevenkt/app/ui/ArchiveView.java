
package ru.sevenkt.app.ui;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.sevenkt.app.ui.handlers.AppEventConstants;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import org.eclipse.swt.widgets.Canvas;

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

	private CircularBufferDataProvider traceDataProvider;

	@Inject
	public ArchiveView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Device device) {
		this.device = device;

		broker.subscribe(AppEventConstants.TOPIC_RESPONSE_ARCHIVE, this);
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER | SWT.BOTTOM);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		tabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Таблица");

		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(tabFolder);
		tbtmNewItem_1.setControl(scrldfrmNewScrolledform);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		scrldfrmNewScrolledform.setText("Просмотр архива");
		scrldfrmNewScrolledform.getBody().setLayout(new FormLayout());

		Section sctnNewSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(),
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

		Section sctnNewSection_1 = formToolkit.createSection(scrldfrmNewScrolledform.getBody(),
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

		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("График");

		ScrolledForm scrldfrmNewScrolledform_1 = formToolkit.createScrolledForm(tabFolder);
		tbtmNewItem.setControl(scrldfrmNewScrolledform_1);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform_1);
		scrldfrmNewScrolledform_1.setText("График");
		scrldfrmNewScrolledform_1.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Canvas canvas = new Canvas(scrldfrmNewScrolledform_1.getBody(), SWT.NONE);
		formToolkit.adapt(canvas);
		formToolkit.paintBordersFor(canvas);
		
		final LightweightSystem lws = new LightweightSystem(canvas);

		// create a new XY Graph.
		XYGraph xyGraph = new XYGraph();
		xyGraph.setTitle("Simple Example");
		// set it as the content of LightwightSystem
		lws.setContents(xyGraph);

		// create a trace data provider, which will provide the data to the
		// trace.
		traceDataProvider = new CircularBufferDataProvider(true);
		traceDataProvider.setBufferSize(100);
		

		// create the trace
		Trace trace = new Trace("Trace1-XY Plot", xyGraph.primaryXAxis, xyGraph.primaryYAxis, traceDataProvider);

		// set trace property
		trace.setPointStyle(PointStyle.XCROSS);

		// add the trace to xyGraph
		xyGraph.addTrace(trace);

	}

	@Override
	public void handleEvent(Event event) {
		
		List<Parameters> parameters = (List<Parameters>) event.getProperty(AppEventConstants.ARCHIVE_PARAMETERS);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		if (!parameters.isEmpty()) {
			parameters.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
			TableViewerColumn dateViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
			dateViewerColumn.setLabelProvider(new ColumnLabelProvider() {
				public Image getImage(Object element) {
					// TODO Auto-generated method stub
					return null;
				}

				public String getText(Object element) {
					return element == null ? "" : ((TableRow) element).getDateTime().format(formatter);
				}
			});
			TableColumn dateColumn = dateViewerColumn.getColumn();
			dateColumn.setWidth(100);
			dateColumn.setText("Дата");
			for (Parameters parameter : parameters) {
				TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
				tableViewerColumn.setLabelProvider(new ArchiveColumnLabelProvider(parameter));
				TableColumn column = tableViewerColumn.getColumn();
				column.setWidth(100);
				column.setText(parameter.getName());
			}
		}
		List<?> input=(List<?>) event.getProperty(AppEventConstants.TABLE_ROWS);
		tableViewer.setInput(input);
		traceDataProvider.setCurrentXDataArray(new double[] { 10, 23, 34, 45, 56, 78, 88, 99 });
		traceDataProvider.setCurrentYDataArray(new double[] { 11, 44, 55, 45, 88, 98, 52, 23 });
	}
}