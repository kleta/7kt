package ru.sevenkt.app.ui.forms;

import java.util.Date;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.wb.swt.ResourceManager;

public class ReportPeriodDialog extends Dialog {

	private Date dateFrom;

	private Date dateTo;

	private String reportFormatt="pdf";

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ReportPeriodDialog(Shell parentShell) {
		super(parentShell);

	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		Label label = new Label(container, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		label.setText("Начало:");

		CDateTime cDateTimeFrom = new CDateTime(container, CDT.BORDER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		cDateTimeFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateFrom = cDateTimeFrom.getSelection();
			}
		});
		FormData fd_cDateTimeFrom = new FormData();
		fd_cDateTimeFrom.bottom = new FormAttachment(label, 0, SWT.BOTTOM);
		cDateTimeFrom.setLayoutData(fd_cDateTimeFrom);

		Label label_1 = new Label(container, SWT.NONE);
		fd_label.bottom = new FormAttachment(label_1, -10);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(0, 43);
		fd_label_1.left = new FormAttachment(0, 10);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Окончание:");

		CDateTime cDateTimeTo = new CDateTime(container, CDT.BORDER | CDT.DROP_DOWN | CDT.DATE_MEDIUM);
		cDateTimeTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dateTo = cDateTimeTo.getSelection();
			}
		});
		fd_cDateTimeFrom.right = new FormAttachment(cDateTimeTo, 0, SWT.RIGHT);
		FormData fd_cDateTimeTo = new FormData();
		fd_cDateTimeTo.top = new FormAttachment(cDateTimeFrom, 6);
		fd_cDateTimeTo.left = new FormAttachment(label_1, 27);
		cDateTimeTo.setLayoutData(fd_cDateTimeTo);

		Label label_2 = new Label(container, SWT.NONE);
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(label_1, 16);
		fd_label_2.left = new FormAttachment(label, 0, SWT.LEFT);
		label_2.setLayoutData(fd_label_2);
		label_2.setText("Формат отчёта:");

		Combo combo = new Combo(container, SWT.READ_ONLY);
		combo.setItems(new String[] { "pdf", "html", "docx", "xls" });

		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(cDateTimeFrom, 0, SWT.RIGHT);
		fd_combo.top = new FormAttachment(cDateTimeTo, 4);
		fd_combo.left = new FormAttachment(label_2, 6);
		combo.setLayoutData(fd_combo);
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				reportFormatt=combo.getText();

			}
		});
		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setImage(ResourceManager.getPluginImage("ru.7kt.app", "icons/report.png"));
		super.configureShell(newShell);
		newShell.setText("Параметры отчёта");
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		Button button = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		button.setText("Отмена");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(243, 191);
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getReportFormatt() {
		return reportFormatt;
	}

	public void setReportFormatt(String reportFormatt) {
		this.reportFormatt = reportFormatt;
	}
	
}
