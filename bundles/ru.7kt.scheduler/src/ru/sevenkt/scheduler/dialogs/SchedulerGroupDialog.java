package ru.sevenkt.scheduler.dialogs;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.field.expression.FieldExpressionFactory.always;
import static com.cronutils.model.field.expression.FieldExpressionFactory.between;
import static com.cronutils.model.field.expression.FieldExpressionFactory.every;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;
import static com.cronutils.model.field.expression.FieldExpressionFactory.questionMark;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import com.cronutils.builder.CronBuilder;
import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronField;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.model.field.constraint.FieldConstraints;
import com.cronutils.model.field.expression.FieldExpression;
import com.cronutils.model.field.expression.visitor.ValidationFieldExpressionVisitor;
import com.cronutils.parser.CronParser;
import com.cronutils.parser.FieldParser;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.domain.ArchiveTypes;
import org.eclipse.core.databinding.beans.BeanProperties;

public class SchedulerGroupDialog extends TitleAreaDialog implements PropertyChangeListener {
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return ResourceManager.getPluginImage("ru.7kt.app", "icons/pda.png");
		}

		public String getColumnText(Object element, int columnIndex) {
			Device device = (Device) element;
			String name = device.getDeviceName() + " №" + device.getSerialNum();
			return name;
		}
	}

	private DataBindingContext m_bindingContext;
	private Text nameText;
	private Text minuteText;
	private Text hourText;
	private Text dayText;
	private Text monthText;
	private Text dayOfWeekText;
	private SchedulerData schedulerData;
	private List<Device> devices;
	private Button okButton;
	private CheckboxTableViewer checkboxTableViewer;
	private Spinner spinner;
	private Button monthButton;
	private Button dayButton;
	private Button hourButton;
	private Label labelPreview;
	private Cron cron;
	private Combo combo;
	private Button extendRadioButton;
	private Button baseRadioButton;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SchedulerGroupDialog(Shell parentShell, SchedulerData data, List<Device> devices) {
		super(parentShell);
		this.devices = devices;
		cron = CronBuilder.cron(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ)).withYear(always())
				.withDoM(between(1, 3)).withMonth(always()).withDoW(questionMark()).withHour(always())
				.withMinute(every(5)).withSecond(on(0)).instance();
		schedulerData = data;
		schedulerData.addPropertyChangeListener(this);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Настройка группы опроса");
		setTitleImage(ResourceManager.getPluginImage("ru.7kt.scheduler", "icons/schedule_file_64.png"));
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(0, 167);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);

		Label label = new Label(composite, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 13);
		label.setLayoutData(fd_label);
		label.setText("Наименование:");

		nameText = new Text(composite, SWT.BORDER);

		FormData fd_nameText = new FormData();
		fd_nameText.left = new FormAttachment(label, 6);
		nameText.setLayoutData(fd_nameText);

		spinner = new Spinner(composite, SWT.BORDER);

		fd_nameText.bottom = new FormAttachment(spinner, -6);
		FormData fd_spinner = new FormData();
		fd_spinner.top = new FormAttachment(0, 35);
		fd_spinner.right = new FormAttachment(0, 155);
		fd_spinner.left = new FormAttachment(0, 91);
		spinner.setLayoutData(fd_spinner);

		Label label_1 = new Label(composite, SWT.NONE);
		fd_label.left = new FormAttachment(label_1, 0, SWT.LEFT);
		FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(spinner, 0, SWT.BOTTOM);
		fd_label_1.left = new FormAttachment(0, 10);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Кол-во суток:");

		Group group = new Group(composite, SWT.NONE);
		fd_nameText.right = new FormAttachment(group, 0, SWT.RIGHT);
		group.setText("Параметры чтения");
		FormData fd_group = new FormData();
		fd_group.right = new FormAttachment(100, -10);
		fd_group.left = new FormAttachment(0, 9);
		fd_group.bottom = new FormAttachment(spinner, 99, SWT.BOTTOM);
		fd_group.top = new FormAttachment(spinner, 6);
		group.setLayoutData(fd_group);

		monthButton = new Button(group, SWT.CHECK);

		monthButton.setText("Месячный архив");
		monthButton.setBounds(10, 22, 100, 16);

		dayButton = new Button(group, SWT.CHECK);
		dayButton.setText("Суточный архив");
		dayButton.setSelection(true);
		dayButton.setBounds(10, 44, 96, 16);

		hourButton = new Button(group, SWT.CHECK);
		hourButton.setText("Часовой архив");
		hourButton.setSelection(true);
		hourButton.setBounds(10, 66, 91, 16);

		TabFolder tabFolder = new TabFolder(container, SWT.BORDER);
		fd_composite.right = new FormAttachment(tabFolder, 0, SWT.RIGHT);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.bottom = new FormAttachment(100, -10);
		fd_tabFolder.top = new FormAttachment(composite, 6);
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(0, 453);
		tabFolder.setLayoutData(fd_tabFolder);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("Устройства");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem.setControl(composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		// table = checkboxTableViewer.getTable();
		checkboxTableViewer.setLabelProvider(new TableLabelProvider());
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {

			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				Object[] elements = checkboxTableViewer.getCheckedElements();
				List<Device> selected = Arrays.asList(Arrays.asList(elements).toArray(new Device[elements.length]));
				schedulerData.setSelectedDevice(selected);
				if (validateFields())
					okButton.setEnabled(true);
				else
					okButton.setEnabled(false);
			}
		});

		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("Расписание");

		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tabItem_1.setControl(composite_2);
		composite_2.setLayout(new FormLayout());

		combo = new Combo(composite_2, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				switch (combo.getText()) {
				case "Каждый день":
					minuteText.setText("0");
					hourText.setText("0");
					dayText.setText("*");
					monthText.setText("*");
					dayOfWeekText.setText("?");
					break;
				case "Каждую неделю":
					minuteText.setText("0");
					hourText.setText("0");
					dayText.setText("?");
					monthText.setText("*");
					dayOfWeekText.setText("1");
					break;
				case "Каждый месяц":
					minuteText.setText("0");
					hourText.setText("0");
					dayText.setText("1");
					monthText.setText("*");
					dayOfWeekText.setText("?");
					break;
				default:
					break;
				}
				String desc = getCronDescription();
				labelPreview.setText("Запуск " + desc);
			}
		});
		combo.setItems(new String[] { "Каждый день", "Каждую неделю", "Каждый месяц" });
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(0, 9);
		fd_combo.right = new FormAttachment(0, 240);
		combo.setLayoutData(fd_combo);

		baseRadioButton = new Button(composite_2, SWT.RADIO);
		baseRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource().equals(baseRadioButton)) {
					combo.setVisible(true);
					minuteText.setEnabled(false);
					hourText.setEnabled(false);
					dayText.setEnabled(false);
					monthText.setEnabled(false);
					dayOfWeekText.setEnabled(false);
					combo.select(0);
					minuteText.setText("0");
					hourText.setText("0");
					dayText.setText("*");
					monthText.setText("*");
					dayOfWeekText.setText("?");
				}
			}
		});
		fd_combo.left = new FormAttachment(baseRadioButton, 26);
		baseRadioButton.setSelection(true);
		FormData fd_baseRadioButton = new FormData();
		fd_baseRadioButton.right = new FormAttachment(0, 91);
		fd_baseRadioButton.top = new FormAttachment(0, 10);
		fd_baseRadioButton.left = new FormAttachment(0, 10);
		baseRadioButton.setLayoutData(fd_baseRadioButton);
		baseRadioButton.setText("Основные");

		extendRadioButton = new Button(composite_2, SWT.RADIO);
		extendRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource().equals(extendRadioButton)) {
					combo.setVisible(false);
					minuteText.setEnabled(true);
					hourText.setEnabled(true);
					dayText.setEnabled(true);
					monthText.setEnabled(true);
					dayOfWeekText.setEnabled(true);
				}
			}
		});
		FormData fd_extendRadioButton = new FormData();
		fd_extendRadioButton.top = new FormAttachment(0, 34);
		fd_extendRadioButton.left = new FormAttachment(0, 10);
		extendRadioButton.setLayoutData(fd_extendRadioButton);
		extendRadioButton.setText("Дополнительные");

		minuteText = new Text(composite_2, SWT.BORDER);

		minuteText.setEnabled(false);
		FormData fd_minuteText = new FormData();
		fd_minuteText.right = new FormAttachment(100, -245);
		minuteText.setLayoutData(fd_minuteText);

		Label label_2 = new Label(composite_2, SWT.NONE);
		fd_minuteText.left = new FormAttachment(label_2, 31);
		fd_minuteText.top = new FormAttachment(label_2, -3, SWT.TOP);
		FormData fd_label_2 = new FormData();
		label_2.setLayoutData(fd_label_2);
		label_2.setText("Минуты:");

		hourText = new Text(composite_2, SWT.BORDER);

		hourText.setEnabled(false);
		FormData fd_hourText = new FormData();
		fd_hourText.top = new FormAttachment(minuteText, 6);
		fd_hourText.right = new FormAttachment(minuteText, 0, SWT.RIGHT);
		combo.select(0);
		hourText.setLayoutData(fd_hourText);

		Label label_3 = new Label(composite_2, SWT.NONE);
		fd_hourText.left = new FormAttachment(label_3, 51);
		fd_label_2.bottom = new FormAttachment(label_3, -12);
		fd_label_2.left = new FormAttachment(0, 46);
		FormData fd_label_3 = new FormData();
		label_3.setLayoutData(fd_label_3);
		label_3.setText("Час:");

		dayText = new Text(composite_2, SWT.BORDER);

		dayText.setEnabled(false);
		FormData fd_dayText = new FormData();
		fd_dayText.left = new FormAttachment(combo, 0, SWT.LEFT);
		fd_dayText.right = new FormAttachment(100, -245);
		dayText.setLayoutData(fd_dayText);

		Label label_4 = new Label(composite_2, SWT.NONE);
		fd_dayText.top = new FormAttachment(label_4, -3, SWT.TOP);
		fd_label_3.bottom = new FormAttachment(label_4, -12);
		fd_label_3.left = new FormAttachment(0, 46);
		FormData fd_label_4 = new FormData();
		label_4.setLayoutData(fd_label_4);
		label_4.setText("День:");

		monthText = new Text(composite_2, SWT.BORDER);

		monthText.setEnabled(false);
		FormData fd_monthText = new FormData();
		fd_monthText.left = new FormAttachment(combo, 0, SWT.LEFT);
		fd_monthText.right = new FormAttachment(100, -245);
		monthText.setLayoutData(fd_monthText);

		Label label_5 = new Label(composite_2, SWT.NONE);
		fd_monthText.top = new FormAttachment(label_5, -3, SWT.TOP);
		fd_label_4.bottom = new FormAttachment(label_5, -12);
		fd_label_4.left = new FormAttachment(0, 46);
		FormData fd_label_5 = new FormData();
		label_5.setLayoutData(fd_label_5);
		label_5.setText("Месяц:");

		dayOfWeekText = new Text(composite_2, SWT.BORDER);

		dayOfWeekText.setEnabled(false);
		FormData fd_dayOfWeekText = new FormData();
		fd_dayOfWeekText.right = new FormAttachment(minuteText, 0, SWT.RIGHT);
		fd_dayOfWeekText.left = new FormAttachment(combo, 0, SWT.LEFT);
		dayOfWeekText.setLayoutData(fd_dayOfWeekText);

		Label label_6 = new Label(composite_2, SWT.NONE);
		fd_dayOfWeekText.top = new FormAttachment(label_6, -3, SWT.TOP);
		fd_label_5.bottom = new FormAttachment(label_6, -12);
		fd_label_5.left = new FormAttachment(0, 46);
		FormData fd_label_6 = new FormData();
		fd_label_6.top = new FormAttachment(0, 172);
		fd_label_6.left = new FormAttachment(label_2, 0, SWT.LEFT);
		label_6.setLayoutData(fd_label_6);
		label_6.setText("День недели:");

		Label label_7 = new Label(composite_2, SWT.NONE);
		FormData fd_label_7 = new FormData();
		fd_label_7.top = new FormAttachment(0, 198);
		fd_label_7.left = new FormAttachment(baseRadioButton, 0, SWT.LEFT);
		label_7.setLayoutData(fd_label_7);
		label_7.setText("Просмотр");

		labelPreview = new Label(composite_2, SWT.NONE);
		FormData fd_labelPreview = new FormData();
		fd_labelPreview.right = new FormAttachment(100, -10);
		fd_labelPreview.bottom = new FormAttachment(100, -10);
		fd_labelPreview.left = new FormAttachment(0, 46);
		labelPreview.setLayoutData(fd_labelPreview);
		labelPreview.setText("Запуск каждый день: в 00:00");
		initState();
		return area;
	}


	protected boolean validateFields() {
		boolean name = validateName();
		boolean daysCount = validateDaysCount();
		boolean va = validateArchive();
		boolean min = validateMinutes();
		boolean dayOfWeek = validateDayOfWeek();
		boolean month = validateMonth();
		boolean day = validateDay();
		boolean hour = validateHour();

		boolean cronEx = validateCron();
		if (name && daysCount && va && min && hour && day && month && dayOfWeek && cronEx) {
			setErrorMessage(null);
			return true;
		}
		return false;
	}

	private boolean validateCron() {
		CronParser quartzCronParser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(QUARTZ));

		try {
			quartzCronParser.parse(schedulerData.generateCronExpression());
			labelPreview.setText(getCronDescription());
		} catch (Exception e) {
			setErrorMessage(e.getMessage());
			labelPreview.setText("Ошибка");
			return false;
		}
		return true;
	}

	private boolean validateMinutes() {
		CronField minuteField = cron.retrieve(CronFieldName.MINUTE);
		FieldConstraints fc = minuteField.getConstraints();
		try {
			validateCronField(fc, CronFieldName.MINUTE, minuteText.getText());
		} catch (Exception e) {
			setErrorMessage("Поле минуты: " + e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validateHour() {
		CronField minuteField = cron.retrieve(CronFieldName.HOUR);
		FieldConstraints fc = minuteField.getConstraints();
		try {
			validateCronField(fc, CronFieldName.HOUR, hourText.getText());
		} catch (Exception e) {
			setErrorMessage("Поле часы: " + e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validateDay() {
		CronField minuteField = cron.retrieve(CronFieldName.DAY_OF_MONTH);
		FieldConstraints fc = minuteField.getConstraints();
		try {
			validateCronField(fc, CronFieldName.DAY_OF_MONTH, dayText.getText());
		} catch (Exception e) {
			setErrorMessage("Поле день: " + e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validateMonth() {
		CronField minuteField = cron.retrieve(CronFieldName.MONTH);
		FieldConstraints fc = minuteField.getConstraints();
		try {
			validateCronField(fc, CronFieldName.MONTH, monthText.getText());
		} catch (Exception e) {
			setErrorMessage("Поле месяц: " + e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validateDayOfWeek() {
		CronField minuteField = cron.retrieve(CronFieldName.DAY_OF_WEEK);
		FieldConstraints fc = minuteField.getConstraints();
		try {
			validateCronField(fc, CronFieldName.DAY_OF_WEEK, dayOfWeekText.getText());
		} catch (Exception e) {
			setErrorMessage("Поле день недели: " + e.getMessage());
			return false;
		}
		return true;
	}

	private void validateCronField(FieldConstraints fc, CronFieldName fn, String text) throws Exception {
		try {
			FieldParser fp = new FieldParser(fc);
			FieldExpression fe = fp.parse(text);
			fe.accept(new ValidationFieldExpressionVisitor(
					cron.getCronDefinition().getFieldDefinition(fn).getConstraints(),
					cron.getCronDefinition().isStrictRanges()));
		} catch (Exception e) {
			throw e;
		}
	}

	private boolean validateArchive() {
		boolean ms = monthButton.getSelection();
		boolean ds = dayButton.getSelection();
		boolean hs = hourButton.getSelection();
		if (ms || ds || hs)
			return true;
		setErrorMessage("Необходимо отметить архивы для чтения");
		return false;
	}

	private boolean validateName() {
		if (nameText.getText() != null && !nameText.getText().equals("")) {
			return true;
		} else {
			setErrorMessage("Введите наименование группы");
			return false;
		}
	}

	private boolean validateDaysCount() {
		int i = spinner.getSelection();
		if (i == 0) {
			setErrorMessage("Количество суток считывания должно быть больше 0");
			return false;
		}

		return true;
	}

	protected String getCronDescription() {
		CronDescriptor descriptor = CronDescriptor.instance(new Locale("ru", "RU"));
		CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		CronParser parser = new CronParser(cronDefinition);
		String description = descriptor.describe(parser.parse(schedulerData.generateCronExpression()));
		return description;
	}

	private void initState() {
		checkboxTableViewer.setInput(devices);
		labelPreview.setText(getCronDescription());
		List<Device> sds = schedulerData.getSelectedDevice();
		if (sds != null) {
			for (Device device : sds) {
				checkboxTableViewer.setChecked(device, true);
			}
		}
		String cronStr = schedulerData.generateCronExpression();
		switch (cronStr) {
		case "0 0 0 * * ? *":
			combo.setEnabled(true);
			minuteText.setEnabled(false);
			hourText.setEnabled(false);
			dayText.setEnabled(false);
			monthText.setEnabled(false);
			dayOfWeekText.setEnabled(false);
			combo.select(0);
			break;
		case "0 0 0 ? * 1 *":
			combo.setEnabled(true);
			minuteText.setEnabled(false);
			hourText.setEnabled(false);
			dayText.setEnabled(false);
			monthText.setEnabled(false);
			dayOfWeekText.setEnabled(false);
			combo.select(1);
			break;
		case "0 0 0 1 * ? *":
			combo.setEnabled(true);
			minuteText.setEnabled(false);
			hourText.setEnabled(false);
			dayText.setEnabled(false);
			monthText.setEnabled(false);
			dayOfWeekText.setEnabled(false);
			combo.select(2);
			break;
		default:
			combo.setVisible(false);
			minuteText.setEnabled(true);
			hourText.setEnabled(true);
			dayText.setEnabled(true);
			monthText.setEnabled(true);
			dayOfWeekText.setEnabled(true);
			extendRadioButton.setSelection(true);
			baseRadioButton.setSelection(false);
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setEnabled(false);
		Button button = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		button.setText("Отменить");
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(468, 585);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Настройка группы");
	}

	@Override
	protected void okPressed() {
		collectInfo();
		super.okPressed();
	}

	private void collectInfo() {
		Object[] elements = checkboxTableViewer.getCheckedElements();
		List<Device> selectedDevices = Arrays.asList(Arrays.asList(elements).toArray(new Device[elements.length]));
	}

	public static void main(String... args) {
		Realm re = SWTObservables.getRealm(Display.getDefault());
		Realm.runWithDefault(re, new Runnable() {

			@Override
			public void run() {
				Shell shell = Display.getDefault().getActiveShell();
				List<Device> devices = new ArrayList<>();
				Device d = new Device();
				d.setDeviceName("ntcn");
				devices.add(d);
				d = new Device();
				d.setDeviceName("qwdfqw;");
				devices.add(d);
				SchedulerData data = new SchedulerData("0", "0", "*", "*", "?");
				SchedulerGroupDialog dialog = new SchedulerGroupDialog(shell, data, devices);
				if (dialog.open() == Window.OK) {
					data = dialog.getSchedulerData();
					String ex = data.generateCronExpression();
					System.out.println(ex);
				}

			}
		});

	}

	public SchedulerData getSchedulerData() {
		return schedulerData;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (validateFields())
			okButton.setEnabled(true);
		else
			okButton.setEnabled(false);
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextText_1ObserveWidget = WidgetProperties.text(SWT.Modify).observe(minuteText);
		IObservableValue minutesSchedulerDataObserveValue = BeanProperties.value("minutes").observe(schedulerData);
		bindingContext.bindValue(observeTextText_1ObserveWidget, minutesSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeTextText_2ObserveWidget = WidgetProperties.text(SWT.Modify).observe(hourText);
		IObservableValue hoursSchedulerDataObserveValue = BeanProperties.value("hours").observe(schedulerData);
		bindingContext.bindValue(observeTextText_2ObserveWidget, hoursSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeTextText_3ObserveWidget = WidgetProperties.text(SWT.Modify).observe(dayText);
		IObservableValue dayOfMonthSchedulerDataObserveValue = BeanProperties.value("dayOfMonth")
				.observe(schedulerData);
		bindingContext.bindValue(observeTextText_3ObserveWidget, dayOfMonthSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeTextText_4ObserveWidget = WidgetProperties.text(SWT.Modify).observe(monthText);
		IObservableValue monthSchedulerDataObserveValue = BeanProperties.value("month").observe(schedulerData);
		bindingContext.bindValue(observeTextText_4ObserveWidget, monthSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.Modify).observe(dayOfWeekText);
		IObservableValue dayOfWeekSchedulerDataObserveValue = BeanProperties.value("dayOfWeek").observe(schedulerData);
		bindingContext.bindValue(observeTextText_5ObserveWidget, dayOfWeekSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeTextNameTextObserveWidget = WidgetProperties.text(SWT.Modify).observe(nameText);
		IObservableValue nameSchedulerDataObserveValue = BeanProperties.value("name").observe(schedulerData);
		bindingContext.bindValue(observeTextNameTextObserveWidget, nameSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeSelectionSpinnerObserveWidget = WidgetProperties.selection().observe(spinner);
		IObservableValue dayShiftSchedulerDataObserveValue = BeanProperties.value("dayShift").observe(schedulerData);
		bindingContext.bindValue(observeSelectionSpinnerObserveWidget, dayShiftSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeSelectionMonthButtonObserveWidget = WidgetProperties.selection().observe(monthButton);
		IObservableValue monthCheckSchedulerDataObserveValue = BeanProperties.value("monthCheck")
				.observe(schedulerData);
		bindingContext.bindValue(observeSelectionMonthButtonObserveWidget, monthCheckSchedulerDataObserveValue, null,
				null);
		//
		IObservableValue observeSelectionDayButtonObserveWidget = WidgetProperties.selection().observe(dayButton);
		IObservableValue dayCheckSchedulerDataObserveValue = BeanProperties.value("dayCheck").observe(schedulerData);
		bindingContext.bindValue(observeSelectionDayButtonObserveWidget, dayCheckSchedulerDataObserveValue, null, null);
		//
		IObservableValue observeSelectionHourButtonObserveWidget = WidgetProperties.selection().observe(hourButton);
		IObservableValue hourCheckSchedulerDataObserveValue = BeanProperties.value("hourCheck").observe(schedulerData);
		bindingContext.bindValue(observeSelectionHourButtonObserveWidget, hourCheckSchedulerDataObserveValue, null,
				null);
		//
		return bindingContext;
	}
}
