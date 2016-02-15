package ru.sevenkt.app.ui.forms;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.db.entities.Device;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;

public class DeviceForm extends Dialog {
	private DataBindingContext m_bindingContext;

	private Device device;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;
	private Text text_7;
	private Text text_8;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DeviceForm(Shell parentShell, Device dev) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
		device=dev;
	}

	/**
	 * Create contents of the dialog.
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
		fd_text.top = new FormAttachment(0, 4);
		fd_text.left = new FormAttachment(label, 85);
		text.setLayoutData(fd_text);
		
		Label label_1 = new Label(container, SWT.NONE);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(label, 9);
		fd_label_1.left = new FormAttachment(label, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Серийный номер:");
		
		Label label_2 = new Label(container, SWT.NONE);
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(label_1, 12);
		fd_label_2.left = new FormAttachment(label, 0, SWT.LEFT);
		label_2.setLayoutData(fd_label_2);
		label_2.setText("Версия устройства:");
		
		Label label_3 = new Label(container, SWT.NONE);
		FormData fd_label_3 = new FormData();
		fd_label_3.top = new FormAttachment(label_2, 12);
		fd_label_3.right = new FormAttachment(label_1, 0, SWT.RIGHT);
		label_3.setLayoutData(fd_label_3);
		label_3.setText("Время наработки:");
		
		Label label_4 = new Label(container, SWT.NONE);
		FormData fd_label_4 = new FormData();
		fd_label_4.top = new FormAttachment(label_3, 12);
		fd_label_4.left = new FormAttachment(label, 0, SWT.LEFT);
		label_4.setLayoutData(fd_label_4);
		label_4.setText("Температура холод. воды:");
		
		Label label_5 = new Label(container, SWT.NONE);
		FormData fd_label_5 = new FormData();
		fd_label_5.top = new FormAttachment(label_4, 12);
		fd_label_5.left = new FormAttachment(label, 0, SWT.LEFT);
		label_5.setLayoutData(fd_label_5);
		label_5.setText("Вес импульса расходомера 1:");
		
		Label label_6 = new Label(container, SWT.NONE);
		label_6.setText("Вес импульса расходомера 2:");
		FormData fd_label_6 = new FormData();
		fd_label_6.top = new FormAttachment(label_5, 12);
		fd_label_6.left = new FormAttachment(label, 0, SWT.LEFT);
		label_6.setLayoutData(fd_label_6);
		
		Label label_7 = new Label(container, SWT.NONE);
		label_7.setText("Вес импульса расходомера 3:");
		FormData fd_label_7 = new FormData();
		fd_label_7.top = new FormAttachment(label_6, 12);
		fd_label_7.left = new FormAttachment(label, 0, SWT.LEFT);
		label_7.setLayoutData(fd_label_7);
		
		Label label_8 = new Label(container, SWT.NONE);
		label_8.setText("Вес импульса расходомера 4:");
		FormData fd_label_8 = new FormData();
		fd_label_8.top = new FormAttachment(label_7, 12);
		fd_label_8.left = new FormAttachment(label, 0, SWT.LEFT);
		label_8.setLayoutData(fd_label_8);
		
		text_1 = new Text(container, SWT.BORDER);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(text, 6);
		fd_text_1.left = new FormAttachment(label_1, 71);
		text_1.setLayoutData(fd_text_1);
		
		text_2 = new Text(container, SWT.BORDER);
		FormData fd_text_2 = new FormData();
		fd_text_2.top = new FormAttachment(label_2, -3, SWT.TOP);
		fd_text_2.left = new FormAttachment(text_1, 0, SWT.LEFT);
		text_2.setLayoutData(fd_text_2);
		
		text_3 = new Text(container, SWT.BORDER);
		FormData fd_text_3 = new FormData();
		fd_text_3.top = new FormAttachment(label_3, -3, SWT.TOP);
		fd_text_3.left = new FormAttachment(text_1, 0, SWT.LEFT);
		text_3.setLayoutData(fd_text_3);
		
		text_4 = new Text(container, SWT.BORDER);
		FormData fd_text_4 = new FormData();
		fd_text_4.top = new FormAttachment(label_4, -3, SWT.TOP);
		fd_text_4.left = new FormAttachment(text_1, 0, SWT.LEFT);
		text_4.setLayoutData(fd_text_4);
		
		text_5 = new Text(container, SWT.BORDER);
		FormData fd_text_5 = new FormData();
		fd_text_5.top = new FormAttachment(label_5, -3, SWT.TOP);
		fd_text_5.right = new FormAttachment(text_1, 0, SWT.RIGHT);
		text_5.setLayoutData(fd_text_5);
		
		text_6 = new Text(container, SWT.BORDER);
		FormData fd_text_6 = new FormData();
		fd_text_6.top = new FormAttachment(label_6, -3, SWT.TOP);
		fd_text_6.right = new FormAttachment(text_1, 0, SWT.RIGHT);
		text_6.setLayoutData(fd_text_6);
		
		text_7 = new Text(container, SWT.BORDER);
		FormData fd_text_7 = new FormData();
		fd_text_7.top = new FormAttachment(label_7, -3, SWT.TOP);
		fd_text_7.right = new FormAttachment(text_1, 0, SWT.RIGHT);
		text_7.setLayoutData(fd_text_7);
		
		text_8 = new Text(container, SWT.BORDER);
		FormData fd_text_8 = new FormData();
		fd_text_8.top = new FormAttachment(label_8, -3, SWT.TOP);
		fd_text_8.left = new FormAttachment(text_1, 0, SWT.LEFT);
		text_8.setLayoutData(fd_text_8);
		
		Label label_9 = new Label(container, SWT.NONE);
		FormData fd_label_9 = new FormData();
		fd_label_9.top = new FormAttachment(label_3, 0, SWT.TOP);
		fd_label_9.left = new FormAttachment(text_3, 6);
		label_9.setLayoutData(fd_label_9);
		label_9.setText("ч");
		
		Label lblc = new Label(container, SWT.NONE);
		FormData fd_lblc = new FormData();
		fd_lblc.top = new FormAttachment(label_4, 0, SWT.TOP);
		fd_lblc.left = new FormAttachment(text_4, 6);
		lblc.setLayoutData(fd_lblc);
		lblc.setText(" °C");
		
		Label label_10 = new Label(container, SWT.NONE);
		fd_text.right = new FormAttachment(label_10, 0, SWT.RIGHT);
		FormData fd_label_10 = new FormData();
		fd_label_10.bottom = new FormAttachment(label_5, 0, SWT.BOTTOM);
		fd_label_10.left = new FormAttachment(text_5, 6);
		label_10.setLayoutData(fd_label_10);
		label_10.setText("л/имп");
		
		Label label_11 = new Label(container, SWT.NONE);
		label_11.setText("л/имп");
		FormData fd_label_11 = new FormData();
		fd_label_11.top = new FormAttachment(label_6, 0, SWT.TOP);
		fd_label_11.left = new FormAttachment(text_6, 6);
		label_11.setLayoutData(fd_label_11);
		
		Label label_12 = new Label(container, SWT.NONE);
		label_12.setText("л/имп");
		FormData fd_label_12 = new FormData();
		fd_label_12.bottom = new FormAttachment(label_7, 0, SWT.BOTTOM);
		fd_label_12.left = new FormAttachment(text_7, 6);
		label_12.setLayoutData(fd_label_12);
		
		Label label_13 = new Label(container, SWT.NONE);
		label_13.setText("л/имп");
		FormData fd_label_13 = new FormData();
		fd_label_13.bottom = new FormAttachment(label_8, 0, SWT.BOTTOM);
		fd_label_13.left = new FormAttachment(text_8, 6);
		label_13.setLayoutData(fd_label_13);

		return container;
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
		return new Point(316, 358);
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
		IObservableValue deviceVersionDeviceObserveValue = PojoProperties.value("deviceVersion").observe(device);
		bindingContext.bindValue(observeTextText_2ObserveWidget, deviceVersionDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_4ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_4);
		IObservableValue tempColdWaterSettingDeviceObserveValue = PojoProperties.value("tempColdWaterSetting").observe(device);
		bindingContext.bindValue(observeTextText_4ObserveWidget, tempColdWaterSettingDeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_5);
		IObservableValue volumeByImpulsSetting1DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting1").observe(device);
		bindingContext.bindValue(observeTextText_5ObserveWidget, volumeByImpulsSetting1DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_6ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_6);
		IObservableValue volumeByImpulsSetting2DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting2").observe(device);
		bindingContext.bindValue(observeTextText_6ObserveWidget, volumeByImpulsSetting2DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_7ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_7);
		IObservableValue volumeByImpulsSetting3DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting3").observe(device);
		bindingContext.bindValue(observeTextText_7ObserveWidget, volumeByImpulsSetting3DeviceObserveValue, null, null);
		//
		IObservableValue observeTextText_8ObserveWidget = WidgetProperties.text(SWT.Modify).observe(text_8);
		IObservableValue volumeByImpulsSetting4DeviceObserveValue = PojoProperties.value("volumeByImpulsSetting4").observe(device);
		bindingContext.bindValue(observeTextText_8ObserveWidget, volumeByImpulsSetting4DeviceObserveValue, null, null);
		//
		return bindingContext;
	}
}
