package ru.sevenkt.app.ui.forms;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.db.entities.Report;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class AssignReportDialog extends TitleAreaDialog {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return super.getText(element);
		}
	}
	private Table table;
	private List<Report> reports;
	private List<String> templates;
	private TableViewer tableViewer;
	private org.eclipse.swt.widgets.List list;
	private ListViewer listViewer;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param reports 
	 * @param templates 
	 */
	public AssignReportDialog(Shell parentShell, List<String> templates, List<Report> reports) {
		super(parentShell);
		this.templates=templates;
		this.reports=reports;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Назначить шаблон отчёта");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		list = listViewer.getList();
		FormData fd_list = new FormData();
		fd_list.bottom = new FormAttachment(100, -10);
		fd_list.left = new FormAttachment(0, 10);
		fd_list.right = new FormAttachment(0, 199);
		list.setLayoutData(fd_list);
		
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(list, 0, SWT.BOTTOM);
		fd_table.right = new FormAttachment(100, -10);
		table.setLayoutData(fd_table);
		
		Label label = new Label(container, SWT.NONE);
		fd_list.top = new FormAttachment(label, 6);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(0, 10);
		fd_label.top = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		label.setText("Список шаблонов");
		
		Label label_1 = new Label(container, SWT.NONE);
		fd_table.top = new FormAttachment(label_1, 6);
		FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(100, -385);
		fd_label_1.left = new FormAttachment(label, 188);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Назначенные шаблоны");
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ReportDialog dialog = new ReportDialog(getShell());
				int retOpen = dialog.open();
				if (retOpen == Window.OK) {
					Report report = dialog.getReport();
					Object s = listViewer.getStructuredSelection().getFirstElement();
					report.setTemplateName(s.toString());
					reports.add(report);
					tableViewer.setInput(reports);
				}
			}
		});
		fd_table.left = new FormAttachment(btnNewButton, 6);
		
		TableViewerColumn nameTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		nameTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				Report report = (Report)element;
				return element == null ? "" : report.getName();
			}
		});
		TableColumn tableColumn = nameTableViewerColumn.getColumn();
		tableColumn.setWidth(149);
		tableColumn.setText("Наименование");
		
		TableViewerColumn typeTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		typeTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				Report report = (Report)element;
				return element == null ? "" : report.getType().getName();
			}
		});
		TableColumn tableColumn_1 = typeTableViewerColumn.getColumn();
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("Тип");
		
		TableViewerColumn templateTableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		templateTableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				Report report = (Report)element;
				return element == null ? "" : report.getTemplateName();
			}
		});
		TableColumn tableColumn_2 = templateTableViewerColumn.getColumn();
		tableColumn_2.setWidth(142);
		tableColumn_2.setText("Шаблон");
		ArrayContentProvider listProvider = new ArrayContentProvider();
		tableViewer.setContentProvider(listProvider);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(0, 45);
		fd_btnNewButton.left = new FormAttachment(list, 6);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("Назначить >>");
		
		Button button = new Button(container, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object s = tableViewer.getStructuredSelection().getFirstElement();
				reports.remove(s);
				tableViewer.setInput(reports);
			}
		});
		FormData fd_button = new FormData();
		fd_button.right = new FormAttachment(btnNewButton, 0, SWT.RIGHT);
		fd_button.top = new FormAttachment(btnNewButton, 6);
		fd_button.left = new FormAttachment(list, 6);
		listViewer.setLabelProvider(new ViewerLabelProvider());
		listViewer.setContentProvider(listProvider);
		listViewer.setInput(templates);
		tableViewer.setInput(reports);
		button.setLayoutData(fd_button);
		button.setText("<< Удалить");
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.setText("Да");
		Button button_1 = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		button_1.setText("Отмена");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(713, 557);
	}
}
