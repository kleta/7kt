package ru.sevenkt.reader.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import ru.sevenkt.db.entities.Connection;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;

public class ConnectionSettingsDialog extends TitleAreaDialog {
	private DataBindingContext m_bindingContext;

	private Text textPhone;

	private Text textInitString;

	private Label labelPhone;

	private Label labelInitString;

	private List<String> ports;

	private Connection connection;
	private Combo combo_1;
	private Combo combo;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @param connection
	 */
	public ConnectionSettingsDialog(Shell parentShell, List<String> ports, Connection connection) {
		super(parentShell);
		setHelpAvailable(false);
		this.ports = ports;
		this.setConnection(connection);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitleImage(ResourceManager.getPluginImage("ru.7kt.reader.ui", "resources/COM64.png"));
		setMessage("Введите параметры соединения  с устройством");
		setTitle("Соединение с устройством");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(container, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 20);
		fd_label.left = new FormAttachment(0, 20);
		label.setLayoutData(fd_label);
		label.setText("Тип подключения:");

		combo = new Combo(container, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (combo.getText().equals("Прямое")) {
					labelPhone.setVisible(false);
					textPhone.setVisible(false);
					labelInitString.setVisible(false);
					textInitString.setVisible(false);
				} else {
					labelPhone.setVisible(true);
					textPhone.setVisible(true);
					labelInitString.setVisible(true);
					textInitString.setVisible(true);
				}
			}
		});

		combo.setItems(new String[] { "Прямое", "Модем" });
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(0, 12);
		fd_combo.left = new FormAttachment(label, 35);
		combo.setLayoutData(fd_combo);

		ComboViewer comboViewer = new ComboViewer(container, SWT.READ_ONLY);
		combo_1 = comboViewer.getCombo();
		fd_combo.right = new FormAttachment(combo_1, 0, SWT.RIGHT);
		FormData fd_combo_1 = new FormData();
		fd_combo_1.top = new FormAttachment(combo, 13);
		combo.select(0);
		fd_combo_1.right = new FormAttachment(100, -10);
		combo_1.setLayoutData(fd_combo_1);

		Label label_1 = new Label(container, SWT.NONE);
		fd_combo_1.left = new FormAttachment(label_1, 95);
		combo_1.select(0);
		comboViewer.setContentProvider(new ArrayContentProvider());
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(0, 52);
		fd_label_1.left = new FormAttachment(0, 20);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Порт:");

		textPhone = new Text(container, SWT.BORDER);
		FormData fd_textPhone = new FormData();
		fd_textPhone.right = new FormAttachment(100, -10);
		textPhone.setLayoutData(fd_textPhone);

		labelPhone = new Label(container, SWT.NONE);
		fd_textPhone.left = new FormAttachment(labelPhone, 77);
		FormData fd_labelPhone = new FormData();
		fd_labelPhone.top = new FormAttachment(0, 85);
		fd_labelPhone.left = new FormAttachment(0, 20);
		labelPhone.setLayoutData(fd_labelPhone);
		labelPhone.setText("Телефон:");

		textInitString = new Text(container, SWT.BORDER);
		fd_textPhone.bottom = new FormAttachment(textInitString, -13);
		FormData fd_textInitString = new FormData();
		fd_textInitString.right = new FormAttachment(100, -10);
		fd_textInitString.top = new FormAttachment(0, 114);
		textInitString.setLayoutData(fd_textInitString);

		labelInitString = new Label(container, SWT.NONE);
		fd_textInitString.left = new FormAttachment(labelInitString, 6);
		FormData fd_labelInitString = new FormData();
		fd_labelInitString.top = new FormAttachment(0, 119);
		fd_labelInitString.left = new FormAttachment(0, 20);
		labelInitString.setLayoutData(fd_labelInitString);
		labelInitString.setText("Строка инициализации:");
		labelPhone.setVisible(false);
		textPhone.setVisible(false);
		labelInitString.setVisible(false);
		textInitString.setVisible(false);
		comboViewer.setInput(ports);
		combo_1.select(0);
		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Отмена", false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 299);
	}

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		List<String> ports = new ArrayList<>();
		ports.add("COM1");
		ports.add("COM3");
		ConnectionSettingsDialog dialog = new ConnectionSettingsDialog(shell, ports, new Connection("Прямое", "COM1", "",""));
		if (dialog.open() == Window.OK) {
			Connection c = dialog.getConnection();
			System.out.println();
		}

	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSelectionCombo_1ObserveWidget = WidgetProperties.selection().observe(combo_1);
		IObservableValue portConnectionObserveValue = PojoProperties.value("port").observe(connection);
		bindingContext.bindValue(observeSelectionCombo_1ObserveWidget, portConnectionObserveValue, null, null);
		//
		IObservableValue observeSelectionComboObserveWidget = WidgetProperties.selection().observe(combo);
		IObservableValue typeConnectionObserveValue = PojoProperties.value("type").observe(connection);
		bindingContext.bindValue(observeSelectionComboObserveWidget, typeConnectionObserveValue, null, null);
		//
		IObservableValue observeTextTextPhoneObserveWidget = WidgetProperties.text(SWT.Modify).observe(textPhone);
		IObservableValue phoneConnectionObserveValue = PojoProperties.value("phone").observe(connection);
		bindingContext.bindValue(observeTextTextPhoneObserveWidget, phoneConnectionObserveValue, null, null);
		//
		IObservableValue observeTextTextInitStringObserveWidget = WidgetProperties.text(SWT.Modify)
				.observe(textInitString);
		IObservableValue initStringConnectionObserveValue = PojoProperties.value("initString").observe(connection);
		bindingContext.bindValue(observeTextTextInitStringObserveWidget, initStringConnectionObserveValue, null, null);
		//
		return bindingContext;
	}
}
