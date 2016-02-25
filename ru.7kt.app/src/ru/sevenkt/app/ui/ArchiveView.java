 
package ru.sevenkt.app.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;

public class ArchiveView {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	@Inject
	public ArchiveView() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		CTabFolder tabFolder = new CTabFolder(parent, SWT.BORDER | SWT.BOTTOM);
		formToolkit.adapt(tabFolder);
		formToolkit.paintBordersFor(tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Таблица");
		
		ScrolledForm scrldfrmNewScrolledform = formToolkit.createScrolledForm(tabFolder);
		tbtmNewItem_1.setControl(scrldfrmNewScrolledform);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform);
		scrldfrmNewScrolledform.setText("Просмотр архива");
		scrldfrmNewScrolledform.getBody().setLayout(new FormLayout());
		
		Section sctnNewSection = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TWISTIE | Section.TITLE_BAR);
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
		
		CDateTime dateTime = new CDateTime(composite, CDT.BORDER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		FormData fd_dateTime = new FormData();
		fd_dateTime.left = new FormAttachment(label, 6);
		fd_dateTime.top = new FormAttachment(0, 2);
		dateTime.setLayoutData(fd_dateTime);
		formToolkit.adapt(dateTime);
		formToolkit.paintBordersFor(dateTime);
		
		Label label_1 = formToolkit.createLabel(composite, "Окончание периода", SWT.NONE);
		fd_dateTime.right = new FormAttachment(label_1, -53);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 0, SWT.TOP);
		label_1.setLayoutData(fd_label_1);
		
		CDateTime dateTime_1 = new CDateTime(composite, CDT.BORDER | CDT.SPINNER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		fd_label_1.right = new FormAttachment(100, -545);
		FormData fd_dateTime_1 = new FormData();
		fd_dateTime_1.right = new FormAttachment(label_1, 136, SWT.RIGHT);
		fd_dateTime_1.top = new FormAttachment(0, 2);
		fd_dateTime_1.left = new FormAttachment(label_1, 6);
		dateTime_1.setLayoutData(fd_dateTime_1);
		formToolkit.adapt(dateTime_1);
		formToolkit.paintBordersFor(dateTime_1);
		
		ComboViewer comboViewer = new ComboViewer(composite, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(dateTime, 0, SWT.TOP);
		fd_combo.right = new FormAttachment(dateTime_1, 199, SWT.RIGHT);
		fd_combo.left = new FormAttachment(dateTime_1, 6);
		combo.setLayoutData(fd_combo);
		formToolkit.paintBordersFor(combo);
		
		Section sctnNewSection_1 = formToolkit.createSection(scrldfrmNewScrolledform.getBody(), Section.TWISTIE | Section.TITLE_BAR);
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
		
		TableViewer tableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn dateViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		dateViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return element == null ? "" : element.toString();
			}
		});
		TableColumn dateColumn = dateViewerColumn.getColumn();
		dateColumn.setWidth(100);
		dateColumn.setText("Дата");
		
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("График");
		
		ScrolledForm scrldfrmNewScrolledform_1 = formToolkit.createScrolledForm(tabFolder);
		tbtmNewItem.setControl(scrldfrmNewScrolledform_1);
		formToolkit.paintBordersFor(scrldfrmNewScrolledform_1);
		scrldfrmNewScrolledform_1.setText("График");
		
	}
}