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
import ru.sevenkt.db.entities.Params;

import org.eclipse.swt.layout.FormAttachment;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;

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
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private ParametersModel paramModel;
	private Button btnE;
	private Button button;
	private Button button_15;
	private Button button_14;
	private Button btnP;
	private Button button_16;
	private Button button_17;
	private Button button_18;
	private Button button_19;
	private Button button_20;
	private Button button_21;
	private Button button_22;
	private Button btnMm;
	private Button button_8;
	private Button button_9;
	private Button button_11;
	private Button button_12;
	private Button btnV;
	private Button btnV_1;
	private Button btnV_2;
	private Button btnV_3;
	private Button button_10;
	private Button button_13;
	private Button button_2;
	private Button button_3;
	private Button button_5;
	private Button button_6;
	private Button button_4;
	private Button button_7;
	private Button button_1;
	private Button button_24;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param dev 
	 */
	public DeviceDialog(Shell parentShell, Device dev, ParametersModel m) {
		super(parentShell);
		setHelpAvailable(false);
		device=dev;
		paramModel=m;
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
		area.setLayout(new GridLayout(1, false));
		
		CTabFolder tabFolder = new CTabFolder(area, SWT.BORDER);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_tabFolder.widthHint = 491;
		tabFolder.setLayoutData(gd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmNewItem = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Основные");
		Composite container = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem.setControl(container);
		container.setLayout(new FormLayout());
		
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
		fd_label_4.top = new FormAttachment(label_2, 12);
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
		FormData fd_text_5 = new FormData();
		fd_text_5.left = new FormAttachment(label_4, 27);
		fd_text_5.top = new FormAttachment(text_3, 6);
		text_5.setLayoutData(fd_text_5);
		
		text_6 = new Text(container, SWT.BORDER);
		fd_text_5.right = new FormAttachment(text_6, 0, SWT.RIGHT);
		fd_label_5.top = new FormAttachment(text_6, 3, SWT.TOP);
		FormData fd_text_6 = new FormData();
		fd_text_6.top = new FormAttachment(text_5, 6);
		fd_text_6.right = new FormAttachment(100, -51);
		fd_text_6.left = new FormAttachment(label_5, 8);
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
		label_10.setText(" °C");
		FormData fd_label_10 = new FormData();
		fd_label_10.top = new FormAttachment(label_4, 0, SWT.TOP);
		fd_label_10.right = new FormAttachment(text, -19, SWT.RIGHT);
		fd_label_10.left = new FormAttachment(text_5, 6);
		label_10.setLayoutData(fd_label_10);
		
		Label label_11 = new Label(container, SWT.NONE);
		label_11.setText("л/имп");
		FormData fd_label_11 = new FormData();
		fd_label_11.top = new FormAttachment(label_8, 0, SWT.TOP);
		fd_label_11.right = new FormAttachment(text, 0, SWT.RIGHT);
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
		fd_text_9.right = new FormAttachment(text_6, 0, SWT.RIGHT);
		fd_text_9.left = new FormAttachment(label_8, 8);
		fd_text_9.top = new FormAttachment(text_8, 6);
		text_9.setLayoutData(fd_text_9);
		
		CTabItem tbtmNewItem_1 = new CTabItem(tabFolder, SWT.NONE);
		tbtmNewItem_1.setText("Параметры");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmNewItem_1.setControl(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(3, false));
		
		Group group_1 = new Group(composite, SWT.NONE);
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group_1.setText("Температура");
		formToolkit.adapt(group_1);
		formToolkit.paintBordersFor(group_1);
		group_1.setLayout(new GridLayout(2, false));
		
		button_2 = new Button(group_1, SWT.CHECK);
		button_2.setText("T1");
		button_2.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_2, true, true);
		
		button_3 = new Button(group_1, SWT.CHECK);
		button_3.setText("T2");
		button_3.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_3, true, true);
		
		button_5 = new Button(group_1, SWT.CHECK);
		button_5.setText("T3");
		button_5.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_5, true, true);
		
		button_6 = new Button(group_1, SWT.CHECK);
		button_6.setText("T4");
		button_6.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_6, true, true);
		
		button_4 = new Button(group_1, SWT.CHECK);
		button_4.setText("T1-T2");
		button_4.setBounds(0, 0, 52, 16);
		formToolkit.adapt(button_4, true, true);
		
		button_7 = new Button(group_1, SWT.CHECK);
		button_7.setText("T3-T4");
		button_7.setBounds(0, 0, 52, 16);
		formToolkit.adapt(button_7, true, true);
		
		Group group_2 = new Group(composite, SWT.NONE);
		group_2.setText("Объем");
		formToolkit.adapt(group_2);
		formToolkit.paintBordersFor(group_2);
		group_2.setLayout(new GridLayout(4, false));
		
		button_8 = new Button(group_2, SWT.CHECK);
		button_8.setText("V1");
		button_8.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_8, true, true);
		
		button_9 = new Button(group_2, SWT.CHECK);
		button_9.setText("V2");
		button_9.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_9, true, true);
		
		button_11 = new Button(group_2, SWT.CHECK);
		button_11.setText("V3");
		button_11.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_11, true, true);
		
		button_12 = new Button(group_2, SWT.CHECK);
		button_12.setText("V4");
		button_12.setBounds(0, 0, 34, 16);
		formToolkit.adapt(button_12, true, true);
		
		btnV = new Button(group_2, SWT.CHECK);
		btnV.setText("V5");
		formToolkit.adapt(btnV, true, true);
		
		btnV_1 = new Button(group_2, SWT.CHECK);
		btnV_1.setText("V6");
		formToolkit.adapt(btnV_1, true, true);
		
		btnV_2 = new Button(group_2, SWT.CHECK);
		btnV_2.setText("V7");
		formToolkit.adapt(btnV_2, true, true);
		
		btnV_3 = new Button(group_2, SWT.CHECK);
		btnV_3.setText("V8");
		formToolkit.adapt(btnV_3, true, true);
		
		button_10 = new Button(group_2, SWT.CHECK);
		button_10.setText("V1-V2");
		button_10.setBounds(0, 0, 52, 16);
		formToolkit.adapt(button_10, true, true);
		
		button_13 = new Button(group_2, SWT.CHECK);
		button_13.setText("V3-V4");
		button_13.setBounds(0, 0, 52, 16);
		formToolkit.adapt(button_13, true, true);
		new Label(group_2, SWT.NONE);
		new Label(group_2, SWT.NONE);
		
		Group group_3 = new Group(composite, SWT.NONE);
		group_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group_3.setText("Масса");
		formToolkit.adapt(group_3);
		formToolkit.paintBordersFor(group_3);
		group_3.setLayout(new GridLayout(2, false));
		
		button_18 = new Button(group_3, SWT.CHECK);
		button_18.setText("M1");
		formToolkit.adapt(button_18, true, true);
		
		button_19 = new Button(group_3, SWT.CHECK);
		button_19.setText("M2");
		formToolkit.adapt(button_19, true, true);
		
		button_20 = new Button(group_3, SWT.CHECK);
		button_20.setText("M3");
		formToolkit.adapt(button_20, true, true);
		
		button_21 = new Button(group_3, SWT.CHECK);
		button_21.setText("M4");
		formToolkit.adapt(button_21, true, true);
		
		button_22 = new Button(group_3, SWT.CHECK);
		button_22.setText("M1-M2");
		formToolkit.adapt(button_22, true, true);
		
		btnMm = new Button(group_3, SWT.CHECK);
		btnMm.setText("M3-M4");
		formToolkit.adapt(btnMm, true, true);
		
		Group group_4 = new Group(composite, SWT.NONE);
		group_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group_4.setText("Давление");
		formToolkit.adapt(group_4);
		formToolkit.paintBordersFor(group_4);
		group_4.setLayout(new GridLayout(2, false));
		
		button_14 = new Button(group_4, SWT.CHECK);
		button_14.setText("P1");
		formToolkit.adapt(button_14, true, true);
		
		btnP = new Button(group_4, SWT.CHECK);
		btnP.setText("P2");
		formToolkit.adapt(btnP, true, true);
		
		button_16 = new Button(group_4, SWT.CHECK);
		button_16.setText("P3");
		formToolkit.adapt(button_16, true, true);
		
		button_17 = new Button(group_4, SWT.CHECK);
		button_17.setText("P4");
		formToolkit.adapt(button_17, true, true);
		
		Group group = new Group(composite, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		group.setText("Энергия");
		formToolkit.adapt(group);
		formToolkit.paintBordersFor(group);
		group.setLayout(new GridLayout(1, false));
		
		btnE = new Button(group, SWT.CHECK);
		btnE.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnE.setText("E1");
		formToolkit.adapt(btnE, true, true);
		
		button = new Button(group, SWT.CHECK);
		button.setText("E2");
		formToolkit.adapt(button, true, true);
		new Label(composite, SWT.NONE);
		
		button_24 = new Button(composite, SWT.CHECK);
		button_24.setText("Время отсутствия счёта");
		formToolkit.adapt(button_24, true, true);
		
		button_15 = new Button(composite, SWT.CHECK);
		button_15.setText("Время нормальной работы");
		formToolkit.adapt(button_15, true, true);
		
		button_1 = new Button(composite, SWT.CHECK);
		button_1.setText("Код ошибки");
		formToolkit.adapt(button_1, true, true);

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
		return new Point(539, 455);
	}

	public Device getDevice() {
		return device;
	}

	public List<Params> getParams() throws IllegalArgumentException, IllegalAccessException {
		return paramModel.getParams();
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
		IObservableValue observeSelectionBtnEObserveWidget = WidgetProperties.selection().observe(btnE);
		IObservableValue e1ParamModelObserveValue = PojoProperties.value("e1").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnEObserveWidget, e1ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButtonObserveWidget = WidgetProperties.selection().observe(button);
		IObservableValue e2ParamModelObserveValue = PojoProperties.value("e2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButtonObserveWidget, e2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_15ObserveWidget = WidgetProperties.selection().observe(button_15);
		IObservableValue workTimeParamModelObserveValue = PojoProperties.value("workTime").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_15ObserveWidget, workTimeParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_14ObserveWidget = WidgetProperties.selection().observe(button_14);
		IObservableValue p1ParamModelObserveValue = PojoProperties.value("p1").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_14ObserveWidget, p1ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnPObserveWidget = WidgetProperties.selection().observe(btnP);
		IObservableValue p2ParamModelObserveValue = PojoProperties.value("p2").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnPObserveWidget, p2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_16ObserveWidget = WidgetProperties.selection().observe(button_16);
		IObservableValue p3ParamModelObserveValue = PojoProperties.value("p3").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_16ObserveWidget, p3ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_17ObserveWidget = WidgetProperties.selection().observe(button_17);
		IObservableValue p4ParamModelObserveValue = PojoProperties.value("p4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_17ObserveWidget, p4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_18ObserveWidget = WidgetProperties.selection().observe(button_18);
		IObservableValue m1ParamModelObserveValue = PojoProperties.value("m1").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_18ObserveWidget, m1ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_19ObserveWidget = WidgetProperties.selection().observe(button_19);
		IObservableValue m2ParamModelObserveValue = PojoProperties.value("m2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_19ObserveWidget, m2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_20ObserveWidget = WidgetProperties.selection().observe(button_20);
		IObservableValue m3ParamModelObserveValue = PojoProperties.value("m3").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_20ObserveWidget, m3ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_21ObserveWidget = WidgetProperties.selection().observe(button_21);
		IObservableValue m4ParamModelObserveValue = PojoProperties.value("m4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_21ObserveWidget, m4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_22ObserveWidget = WidgetProperties.selection().observe(button_22);
		IObservableValue m1Subm2ParamModelObserveValue = PojoProperties.value("m1Subm2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_22ObserveWidget, m1Subm2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_23ObserveWidget = WidgetProperties.selection().observe(btnMm);
		IObservableValue m3Subm4ParamModelObserveValue = PojoProperties.value("m3Subm4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_23ObserveWidget, m3Subm4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_8ObserveWidget = WidgetProperties.selection().observe(button_8);
		IObservableValue v1ParamModelObserveValue = PojoProperties.value("v1").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_8ObserveWidget, v1ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_9ObserveWidget = WidgetProperties.selection().observe(button_9);
		IObservableValue v2ParamModelObserveValue = PojoProperties.value("v2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_9ObserveWidget, v2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_11ObserveWidget = WidgetProperties.selection().observe(button_11);
		IObservableValue v3ParamModelObserveValue = PojoProperties.value("v3").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_11ObserveWidget, v3ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_12ObserveWidget = WidgetProperties.selection().observe(button_12);
		IObservableValue v4ParamModelObserveValue = PojoProperties.value("v4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_12ObserveWidget, v4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnVObserveWidget = WidgetProperties.selection().observe(btnV);
		IObservableValue v5ParamModelObserveValue = PojoProperties.value("v5").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnVObserveWidget, v5ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnV_1ObserveWidget = WidgetProperties.selection().observe(btnV_1);
		IObservableValue v6ParamModelObserveValue = PojoProperties.value("v6").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnV_1ObserveWidget, v6ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnV_2ObserveWidget = WidgetProperties.selection().observe(btnV_2);
		IObservableValue v7ParamModelObserveValue = PojoProperties.value("v7").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnV_2ObserveWidget, v7ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnV_3ObserveWidget = WidgetProperties.selection().observe(btnV_3);
		IObservableValue v8ParamModelObserveValue = PojoProperties.value("v8").observe(paramModel);
		bindingContext.bindValue(observeSelectionBtnV_3ObserveWidget, v8ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_10ObserveWidget = WidgetProperties.selection().observe(button_10);
		IObservableValue v1Subv2ParamModelObserveValue = PojoProperties.value("v1Subv2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_10ObserveWidget, v1Subv2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_13ObserveWidget = WidgetProperties.selection().observe(button_13);
		IObservableValue v3Subv4ParamModelObserveValue = PojoProperties.value("v3Subv4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_13ObserveWidget, v3Subv4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_2ObserveWidget = WidgetProperties.selection().observe(button_2);
		IObservableValue t1ParamModelObserveValue = PojoProperties.value("t1").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_2ObserveWidget, t1ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_3ObserveWidget = WidgetProperties.selection().observe(button_3);
		IObservableValue t2ParamModelObserveValue = PojoProperties.value("t2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_3ObserveWidget, t2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_5ObserveWidget = WidgetProperties.selection().observe(button_5);
		IObservableValue t3ParamModelObserveValue = PojoProperties.value("t3").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_5ObserveWidget, t3ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_6ObserveWidget = WidgetProperties.selection().observe(button_6);
		IObservableValue t4ParamModelObserveValue = PojoProperties.value("t4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_6ObserveWidget, t4ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_4ObserveWidget = WidgetProperties.selection().observe(button_4);
		IObservableValue t1Subt2ParamModelObserveValue = PojoProperties.value("t1Subt2").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_4ObserveWidget, t1Subt2ParamModelObserveValue, null, null);
		//
		IObservableValue observeSelectionButton_7ObserveWidget = WidgetProperties.selection().observe(button_7);
		IObservableValue t3Subt4ParamModelObserveValue = PojoProperties.value("t3Subt4").observe(paramModel);
		bindingContext.bindValue(observeSelectionButton_7ObserveWidget, t3Subt4ParamModelObserveValue, null, null);
		//
		return bindingContext;
	}
}
