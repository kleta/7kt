
package ru.sevenkt.app.ui;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.sevenkt.app.ui.handlers.AppEventConstants;
import ru.sevenkt.db.entities.Journal;

public class JournalSettingsView implements EventHandler {
	private Table table;
	private IEventBroker broker;
	private TableViewer tableViewer;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

	@Inject
	public JournalSettingsView(IEventBroker br) {
		broker = br;
		broker.subscribe(AppEventConstants.TOPIC_SHOW_JOURNAL, this);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn dateTblViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		dateTblViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				// TODO Auto-generated method stub
				return element == null ? "" : ((Journal) element).getDateTime().format(formatter);
			}
		});
		TableColumn tblclmnDatecolumn = dateTblViewerColumn.getColumn();
		tblclmnDatecolumn.setWidth(100);
		tblclmnDatecolumn.setText("Дата");

		TableViewerColumn workTblViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		workTblViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				// TODO Auto-generated method stub
				return element == null ? "" : ((Journal) element).getWorkHour()+"";
			}
		});
		TableColumn tblclmnWorkhourcolumn = workTblViewerColumn.getColumn();
		tblclmnWorkhourcolumn.setWidth(100);
		tblclmnWorkhourcolumn.setText("Наработка");

		TableViewerColumn eventTblViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		eventTblViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				// TODO Auto-generated method stub
				return element == null ? "" : ((Journal) element).getEvent().getName();
			}
		});
		TableColumn tblclmnEventcolumn = eventTblViewerColumn.getColumn();
		tblclmnEventcolumn.setMoveable(true);
		tblclmnEventcolumn.setWidth(600);
		tblclmnEventcolumn.setText("Событие");
		tableViewer.setContentProvider(new ArrayContentProvider());

	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleEvent(Event event) {
		List<Journal> input = (List<Journal>) event.getProperty(AppEventConstants.EVENTS);
		tableViewer.setInput(input);
	}

}