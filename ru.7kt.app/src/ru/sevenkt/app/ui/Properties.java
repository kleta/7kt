 
package ru.sevenkt.app.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;

public class Properties {
	private Table table;
	@Inject
	public Properties() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn = tableViewerColumn.getColumn();
		tableColumn.setWidth(100);
		tableColumn.setText("Наименование");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = tableViewerColumn_1.getColumn();
		tableColumn_1.setWidth(200);
		tableColumn_1.setAlignment(SWT.CENTER);
		tableColumn_1.setText("Значение");
		
	}
	
	
	
	
}