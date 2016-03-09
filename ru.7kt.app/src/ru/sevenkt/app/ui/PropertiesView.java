
package ru.sevenkt.app.ui;

import java.util.ArrayList;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ru.sevenkt.db.entities.Device;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.wb.swt.TableViewerColumnSorter;

public class PropertiesView {

	private Table table;
	private TableViewer tableViewer;
	private TableColumn valueTableColumn;
	private TableColumn nameTableColumn;

	@Inject
	public PropertiesView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLocation(85, 0);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		new TableViewerColumnSorter(tableViewerColumn) {
			@Override
			protected int doCompare(Viewer viewer, Object e1, Object e2) {
				// TODO Remove this method, if your getValue(Object) returns
				// Comparable.
				// Typical Comparable are String, Integer, Double, etc.
				return super.doCompare(viewer, e1, e2);
			}

			@Override
			protected Object getValue(Object o) {
				// TODO remove this method, if your EditingSupport returns value
				return super.getValue(o);
			}
		};
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				Properties e = (Properties) element;
				Object key = e.keys().nextElement();
				return e.keys().nextElement().toString();
			}
		});
		nameTableColumn = tableViewerColumn.getColumn();
		nameTableColumn.setWidth(215);
		nameTableColumn.setText("Наименование");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}

			public String getText(Object element) {
				Properties e = (Properties) element;
				Object key = e.keys().nextElement();
				Object object = e.get(key);
				if(object instanceof Float){
					return Math.round((Float)object)+"";
				}
				return object.toString();
			}
		});
		valueTableColumn = tableViewerColumn_1.getColumn();
		valueTableColumn.setWidth(228);
		valueTableColumn.setText("Значение");
		tableViewer.setContentProvider(new ArrayContentProvider());

	}

	@Inject
	public void setInput(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object)
			throws IllegalArgumentException, IllegalAccessException {
		if (object != null) {
			if (object instanceof Device) {
				tableViewer.setInput(((Device) object).getProperies());
				valueTableColumn.pack();
				nameTableColumn.pack();
			}
			else
				tableViewer.setInput(new ArrayList<>());
		}

	}

}