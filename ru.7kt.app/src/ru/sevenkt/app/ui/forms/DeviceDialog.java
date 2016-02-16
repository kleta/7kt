package ru.sevenkt.app.ui.forms;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Text;

import ru.sevenkt.db.entities.Device;

import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.wb.swt.ResourceManager;

public class DeviceDialog extends TitleAreaDialog {
	private DataBindingContext m_bindingContext;
	private Device device;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private Text text_8;
	private Text text_9;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param device2 
	 */
	public DeviceDialog(Shell parentShell, Device device2) {
		super(parentShell);
		setHelpAvailable(false);
		device=device2;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Установки прибора");
		setTitleImage(ResourceManager.getPluginImage("ru.7kt.app", "icons/7kt64.png"));
		setTitle("Устройство");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(container, SWT.NONE);
		label.setText("Наименование:");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 16);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		
		text = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(label, 85);
		fd_text.right = new FormAttachment(100, -10);
		fd_text.top = new FormAttachment(label, -3, SWT.TOP);
		text.setLayoutData(fd_text);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setText("Серийный номер:");
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 12);
		fd_label_1.left = new FormAttachment(label, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setText("Версия устройства:");
		FormData fd_label_2 = new FormData();
		fd_label_2.left = new FormAttachment(label, 0, SWT.LEFT);
		label_2.setLayoutData(fd_label_2);
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("Температура холод. воды:");
		FormData fd_label_4 = new FormData();
		fd_label_4.left = new FormAttachment(label, 0, SWT.LEFT);
		label_4.setLayoutData(fd_label_4);
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("Вес импульса расходомера 1:");
		FormData fd_label_5 = new FormData();
		fd_label_5.left = new FormAttachment(label, 0, SWT.LEFT);
		label_5.setLayoutData(fd_label_5);
		
		Label label_6 = new Label(container, SWT.NONE);
		label_6.setText("Вес импульса расходомера 2:");
		FormData fd_label_6 = new FormData();
		fd_label_6.left = new FormAttachment(label, 0, SWT.LEFT);
		label_6.setLayoutData(fd_label_6);
		
		Label label_7 = new Label(container, SWT.NONE);
		label_7.setText("Вес импульса расходомера 3:");
		FormData fd_label_7 = new FormData();
		fd_label_7.right = new FormAttachment(label_5, 0, SWT.RIGHT);
		label_7.setLayoutData(fd_label_7);
		
		Label label_8 = new Label(container, SWT.NONE);
		label_8.setText("Вес импульса расходомера 4:");
		FormData fd_label_8 = new FormData();
		fd_label_8.top = new FormAttachment(label_7, 12);
		fd_label_8.right = new FormAttachment(label_5, 0, SWT.RIGHT);
		label_8.setLayoutData(fd_label_8);
		
		text_1 = new Text(container, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_text_1.top = new FormAttachment(text, 6);
		fd_text_1.left = new FormAttachment(label_1, 71);
		text_1.setLayoutData(fd_text_1);
		
		text_2 = new Text(container, SWT.BORDER);
		FormData fd_text_2 = new FormData();
		fd_text_2.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_text_2.top = new FormAttachment(text_1, 6);
		text_2.setLayoutData(fd_text_2);
		
		text_3 = new Text(container, SWT.BORDER);
		FormData fd_text_3 = new FormData();
		fd_text_3.right = new FormAttachment(text, 0, SWT.RIGHT);
		fd_text_3.top = new FormAttachment(text_2, 6);
		fd_text_3.left = new FormAttachment(label_2, 65);
		text_3.setLayoutData(fd_text_3);
		
		text_5 = new Text(container, SWT.BORDER);
		fd_label_4.top = new FormAttachment(text_5, 3, SWT.TOP);
		FormData fd_text_5 = new FormData();
		fd_text_5.left = new FormAttachment(label_4, 27);
		fd_text_5.top = new FormAttachment(text_3, 6);
		text_5.setLayoutData(fd_text_5);
		
		text_6 = new Text(container, SWT.BORDER);
		fd_label_5.top = new FormAttachment(text_6, 3, SWT.TOP);
		FormData fd_text_6 = new FormData();
		fd_text_6.right = new FormAttachment(100, -51);
		fd_text_6.left = new FormAttachment(label_5, 8);
		fd_text_6.top = new FormAttachment(text_5, 6);
		text_6.setLayoutData(fd_text_6);
		
		text_7 = new Text(container, SWT.BORDER);
		fd_label_6.top = new FormAttachment(text_7, 3, SWT.TOP);
		FormData fd_text_7 = new FormData();
		fd_text_7.left = new FormAttachment(label_6, 8);
		fd_text_7.right = new FormAttachment(100, -51);
		fd_text_7.top = new FormAttachment(text_6, 6);
		text_7.setLayoutData(fd_text_7);
		
		text_8 = new Text(container, SWT.BORDER);
		fd_label_7.top = new FormAttachment(text_8, 3, SWT.TOP);
		FormData fd_text_8 = new FormData();
		fd_text_8.left = new FormAttachment(label_7, 8);
		fd_text_8.top = new FormAttachment(text_7, 6);
		text_8.setLayoutData(fd_text_8);
		
		Label label_10 = new Label(container, SWT.NONE);
		fd_text_5.right = new FormAttachment(label_10, -6);
		label_10.setText(" °C");
		FormData fd_label_10 = new FormData();
		fd_label_10.top = new FormAttachment(label_4, 0, SWT.TOP);
		fd_label_10.left = new FormAttachment(0, 367);
		fd_label_10.right = new FormAttachment(0, 383);
		label_10.setLayoutData(fd_label_10);
		
		Label label_11 = new Label(container, SWT.NONE);
		label_11.setText("л/имп");
		FormData fd_label_11 = new FormData();
		fd_label_11.top = new FormAttachment(label_8, 0, SWT.TOP);
		fd_label_11.left = new FormAttachment(label_10, 0, SWT.LEFT);
		label_11.setLayoutData(fd_label_11);
		
		Label label_12 = new Label(container, SWT.NONE);
		fd_text_8.right = new FormAttachment(100, -51);
		label_12.setText("л/имп");
		FormData fd_label_12 = new FormData();
		fd_label_12.top = new FormAttachment(label_7, 0, SWT.TOP);
		fd_label_12.left = new FormAttachment(text_8, 6);
		label_12.setLayoutData(fd_label_12);
		
		Label label_13 = new Label(container, SWT.NONE);
		label_13.setText("л/имп");
		FormData fd_label_13 = new FormData();
		fd_label_13.top = new FormAttachment(label_6, 0, SWT.TOP);
		fd_label_13.left = new FormAttachment(text_7, 6);
		label_13.setLayoutData(fd_label_13);
		
		Label label_14 = new Label(container, SWT.NONE);
		label_14.setText("л/имп");
		FormData fd_label_14 = new FormData();
		fd_label_14.top = new FormAttachment(label_5, 0, SWT.TOP);
		fd_label_14.left = new FormAttachment(text_6, 6);
		label_14.setLayoutData(fd_label_14);
		
		Label label_15 = new Label(container, SWT.NONE);
		fd_label_2.top = new FormAttachment(label_15, 12);
		fd_text_2.left = new FormAttachment(label_15, 134);
		label_15.setText("Схема:");
		FormData fd_label_15 = new FormData();
		fd_label_15.top = new FormAttachment(label_1, 12);
		fd_label_15.left = new FormAttachment(label, 0, SWT.LEFT);
		label_15.setLayoutData(fd_label_15);
		
		text_9 = new Text(container, SWT.BORDER);
		FormData fd_text_9 = new FormData();
		fd_text_9.right = new FormAttachment(label_11, -6);
		fd_text_9.left = new FormAttachment(label_8, 8);
		fd_text_9.top = new FormAttachment(text_8, 6);
		text_9.setLayoutData(fd_text_9);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(418, 455);
	}

	public Device getDevice() {
		return device;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(text);
		IObservableValue deviceNameDeviceObserveValue = PojoProperties.value("deviceName").observe(device);
		bindingContext.bindValue(observeTextTextObserveWidget, deviceNameDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_1ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_1);
		IObservableValue serialNumDeviceObserveValue = PojoProperties.value("serialNum").observe(device);
		bindingContext.bindValue(observeTextText_1ObserveWidget, serialNumDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_2ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_2);
		IObservableValue formulaNumDeviceObserveValue = PojoProperties.value("formulaNum").observe(device);
		bindingContext.bindValue(observeTextText_2ObserveWidget, formulaNumDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_3ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_3);
		IObservableValue deviceVersionDeviceObserveValue = PojoProperties.value("deviceVersion").observe(device);
		bindingContext.bindValue(observeTextText_3ObserveWidget, deviceVersionDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_5);
		IObservableValue tempColdWaterSettingDeviceObserveValue = PojoProperties.value("tempColdWaterSetting").observe(device);
		bindingContext.bindValue(observeTextText_5ObserveWidget, tempColdWaterSettingDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_6ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_6);
		IObservableValue volumeByImpulsSetting1DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting1").observe(device);
		bindingContext.bindValue(observeTextText_6ObserveWidget, volumeByImpulsSetting1DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_7ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_7);
		IObservableValue volumeByImpulsSetting2DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting2").observe(device);
		bindingContext.bindValue(observeTextText_7ObserveWidget, volumeByImpulsSetting2DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_8ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_8);
		IObservableValue volumeByImpulsSetting3DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting3").observe(device);
		bindingContext.bindValue(observeTextText_8ObserveWidget, volumeByImpulsSetting3DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_9ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_9);
		IObservableValue volumeByImpulsSetting4DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting4").observe(device);
		bindingContext.bindValue(observeTextText_9ObserveWidget, volumeByImpulsSetting4DeviceObserveValue, null, null);
		//
		return bindingContext;
	}
}
