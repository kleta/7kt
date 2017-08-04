package ru.sevenkt.scheduler.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.quartz.SchedulerException;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.scheduler.Group;
import ru.sevenkt.scheduler.SchedulerEventConstants;
import ru.sevenkt.scheduler.services.ISchedulerSevice;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;

public class SchedulerView implements EventHandler {
	private Table table;

	@Inject
	private IDBService dbService;

	@Inject
	private IEventBroker broker;

	@Inject
	ESelectionService selectionService;

	@Inject
	@Optional
	ISchedulerSevice schService;

	private static final Image CHECKED = ResourceManager.getPluginImage("ru.7kt.scheduler", "icons/checked.gif");

	private static final Image UNCHECKED = ResourceManager.getPluginImage("ru.7kt.scheduler", "icons/unchecked.gif");

	private CheckboxTableViewer tableViewer;

	private java.util.List<ru.sevenkt.scheduler.Group> groups;

	public SchedulerView() {
		super();
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void createPartControl(Composite parent, ESelectionService s, EMenuService ms) {
		broker.subscribe(SchedulerEventConstants.TOPIC_REFRESH_SCHEDULER_VIEW, this);
		broker.subscribe(SchedulerEventConstants.TOPIC_GROUP_STATE_EXECUTE, this);
		broker.subscribe(SchedulerEventConstants.TOPIC_GROUP_STATE_WAIT, this);
		tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setBounds(97, 174, 85, 85);
		tableViewer.addSelectionChangedListener(listener -> {
			IStructuredSelection selection = tableViewer.getStructuredSelection();
			s.setSelection(selection.getFirstElement());
		});
		tableViewer.addCheckStateListener(listener -> {
			Group gr = (ru.sevenkt.scheduler.Group) listener.getElement();
			boolean val = listener.getChecked();
			gr.setEnabled(val);
			SchedulerGroup schedulerGroup = gr.getSchedulerGroup();
			dbService.saveSchedulerGroup(schedulerGroup);
			if (val)
				gr.setState("Ожидание");
			else
				gr.setState("Остановлено");
			try {
				schService.restart();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			tableViewer.setInput(groups);
		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				broker.post(SchedulerEventConstants.TOPIC_EDIT_SCHEDULER, "");
			}
		});
		ms.registerContextMenu(tableViewer.getControl(), "ru.7kt.scheduler.popupmenu.group");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				return "";
			}
		});
		TableColumn tableColumn_2 = tableViewerColumn_3.getColumn();
		tableColumn_2.setWidth(100);
		tableColumn_2.setText("Активность");
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				Group gr = (ru.sevenkt.scheduler.Group) element;
				return element == null ? "" : gr.getName();
			}
		});
		TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("Наименование");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				Group gr = (ru.sevenkt.scheduler.Group) element;
				return element == null ? "" : gr.getState();
			}
		});
		TableColumn tableColumn = tableViewerColumn_1.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("Состояние");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				Group gr = (ru.sevenkt.scheduler.Group) element;
				return element == null ? "" : gr.getDeviceCount() + "";
			}
		});
		TableColumn tableColumn_1 = tableViewerColumn_2.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("Кол-во устройств");
		tableViewer.setContentProvider(new ArrayContentProvider());
		java.util.List<SchedulerGroup> sgs = dbService.findAllShedulerGroup();
		groups = new ArrayList<>();
		for (SchedulerGroup schedulerGroup : sgs) {
			Group gr = new ru.sevenkt.scheduler.Group(schedulerGroup);	
			groups.add(gr);
		}
		tableViewer.setInput(groups);
		for (Group group : groups) {
				
			if(group.getSchedulerGroup().isEnabled()){
				tableViewer.setChecked(group, true);
			}
			else{
				group.setState("Остановлено");
				
			}
		}
		tableViewer.refresh();

	}

	@Override
	public void handleEvent(Event event) {
		String topic = event.getTopic();
		switch (topic) {
		case SchedulerEventConstants.TOPIC_REFRESH_SCHEDULER_VIEW:
			java.util.List<SchedulerGroup> sgs = dbService.findAllShedulerGroup();
			groups = new ArrayList<>();
			for (SchedulerGroup schedulerGroup : sgs) {
				Group gr = new ru.sevenkt.scheduler.Group(schedulerGroup);
				if(schedulerGroup.isEnabled()){
					tableViewer.setChecked(gr, true);
				}
				else{
					gr.setState("Остановлено");
				}
				groups.add(gr);
			}
			tableViewer.setInput(groups);
			break;
		case SchedulerEventConstants.TOPIC_GROUP_STATE_EXECUTE:
			ArrayList<Group> input = (ArrayList<Group>) tableViewer.getInput();
			Group group = (Group) event.getProperty("org.eclipse.e4.data");
			int i = input.indexOf(group);
			input.get(i).setState("Выполняется");
			tableViewer.setInput(input);
			break;
		case SchedulerEventConstants.TOPIC_GROUP_STATE_WAIT:
			input = (ArrayList<Group>) tableViewer.getInput();
			group = (Group) event.getProperty("org.eclipse.e4.data");
			i = input.indexOf(group);
			input.get(i).setState("Ожидание");
			tableViewer.setInput(input);
			break;
		}

	}
}
