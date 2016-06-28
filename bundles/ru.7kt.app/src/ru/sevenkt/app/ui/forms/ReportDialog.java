package ru.sevenkt.app.ui.forms;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;

import ru.sevenkt.db.entities.Report;
import ru.sevenkt.domain.ArchiveTypes;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.graphics.Image;

public class ReportDialog extends Dialog {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			ArchiveTypes type = (ArchiveTypes)element;
			return type.getName();
		}
	}
	private DataBindingContext m_bindingContext;
	
	private Text text;

	private Report report;
	private ComboViewer comboViewer;

	private List<ArchiveTypes> types;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ReportDialog(Shell parentShell) {
		super(parentShell);
		report=new Report();
		types = Arrays.asList(ArchiveTypes.values());
		
	}

	public Report getReport() {
		return report;
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
		fd_label.top = new FormAttachment(0, 10);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		label.setText("Наименование:");

		text = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(label, 338, SWT.RIGHT);
		fd_text.top = new FormAttachment(0, 4);
		fd_text.left = new FormAttachment(label, 6);
		text.setLayoutData(fd_text);

		Label label_1 = new Label(container, SWT.NONE);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 6);
		fd_label_1.left = new FormAttachment(0, 10);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Тип отчёта:");

		comboViewer = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_combo.top = new FormAttachment(text, 6);
		fd_combo.left = new FormAttachment(text, 0, SWT.LEFT);
		combo.setLayoutData(fd_combo);
		comboViewer.setLabelProvider(new ViewerLabelProvider());
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setInput(types);
		comboViewer.setSelection(new StructuredSelection(types.get(0)));
		return container;
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
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 143);
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue nameReportObserveValue = PojoProperties.value("name").observe(report);
		bindingContext.bindValue(observeTextTextObserveWidget, nameReportObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionComboViewer = ViewerProperties.singleSelection().observe(comboViewer);
		IObservableValue typeReportObserveValue = PojoProperties.value("type").observe(report);
		bindingContext.bindValue(observeSingleSelectionComboViewer, typeReportObserveValue, null, null);
		//
		return bindingContext;
	}
}
