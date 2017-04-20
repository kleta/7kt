package ru.sevenkt.reader.ui.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

import ru.sevenkt.reader.services.Archive7KTC;
import ru.sevenkt.reader.ui.handlers.Read7ktcHandler;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class ImportFrom7KTCDialog extends TitleAreaDialog {

	private Text pathText;
	private ProgressBar progressBar;
	private Composite shell;
	private Label arhiveNameLabel;
	private Read7ktcHandler readHandler;
	private int i;
	private int pbSelection;
	private Button okButton;
	private Table table;
	private CheckboxTableViewer checkboxTableViewer;
	private List<Object> selectedElements = new ArrayList<>();
	protected String selectedPath;
	private Button selectFolderButton;
	private Button showDeletedButton;
	private String saveFolder;
	// private boolean withDeleted;
	private Button saveInFileButton;
	private boolean saveToFile;
	private ComboViewer comboViewer;
	private String comPort;
	private Button buttonRefresh;

	public ImportFrom7KTCDialog(Shell parentShell, Read7ktcHandler handler) {
		super(parentShell);
		readHandler = handler;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		shell = parent;
		setMessage("Выберите порт подключения 7КТС");
		setTitle("Импорт архивов");
		setTitleImage(ResourceManager.getPluginImage("ru.7kt.reader.ui", "resources/rsz_1rsz_27ktc.png"));
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite composite_1 = new Composite(container, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(0, 57);
		fd_composite_1.top = new FormAttachment(0);
		fd_composite_1.left = new FormAttachment(0);
		fd_composite_1.right = new FormAttachment(100);
		composite_1.setLayoutData(fd_composite_1);

		comboViewer = new ComboViewer(composite_1, SWT.READ_ONLY);
		Combo combo = comboViewer.getCombo();
		combo.setBounds(66, 10, 394, 21);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.addSelectionChangedListener(l -> {
			IStructuredSelection selection = comboViewer.getStructuredSelection();
			comPort = (String) selection.getFirstElement();
			if(comPort!=null)
				buttonRefresh.setEnabled(true);
			readArchiveList(comPort);
		});

		Label lblCom = new Label(composite_1, SWT.NONE);
		lblCom.setBounds(10, 13, 50, 13);
		lblCom.setText("COM-порт:");

		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(composite_1, 2);
		fd_composite.left = new FormAttachment(0);
		fd_composite.right = new FormAttachment(100);
		composite.setLayoutData(fd_composite);

		Label label = new Label(composite, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(0, 59);
		fd_label.top = new FormAttachment(0);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		label.setText("Архивы:");

		Button button = new Button(composite, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkboxTableViewer.setAllChecked(true);
				selectedElements=Arrays.asList(checkboxTableViewer.getCheckedElements());
				okButton.setEnabled(true);
			}
		});
		FormData fd_button = new FormData();
		fd_button.right = new FormAttachment(0, 460);
		fd_button.top = new FormAttachment(0, 19);
		fd_button.left = new FormAttachment(0, 387);
		button.setLayoutData(fd_button);
		button.setText("Выбрать все");

		Button button_1 = new Button(composite, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				checkboxTableViewer.setAllChecked(false);
				selectedElements=Arrays.asList(checkboxTableViewer.getCheckedElements());
				okButton.setEnabled(false);
			}
		});
		FormData fd_button_1 = new FormData();
		fd_button_1.right = new FormAttachment(0, 460);
		fd_button_1.top = new FormAttachment(0, 48);
		fd_button_1.left = new FormAttachment(0, 387);
		button_1.setLayoutData(fd_button_1);
		button_1.setText("Отменить");

		buttonRefresh = new Button(composite, SWT.NONE);
		buttonRefresh.setEnabled(false);
		buttonRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = comboViewer.getStructuredSelection();
				comPort = (String) selection.getFirstElement();
				readArchiveList(comPort);

			}
		});
		FormData fd_buttonRefresh = new FormData();
		fd_buttonRefresh.right = new FormAttachment(0, 460);
		fd_buttonRefresh.top = new FormAttachment(0, 77);
		fd_buttonRefresh.left = new FormAttachment(0, 387);
		buttonRefresh.setLayoutData(fd_buttonRefresh);
		buttonRefresh.setText("Обновить");

		Composite composite_2 = new Composite(container, SWT.NONE);
		fd_composite.bottom = new FormAttachment(composite_2, -6);
		composite_2.setLayout(new FormLayout());
		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(0, 348);
		fd_composite_2.bottom = new FormAttachment(100, -10);
		fd_composite_2.right = new FormAttachment(composite_1, 0, SWT.RIGHT);
		fd_composite_2.left = new FormAttachment(composite_1, 0, SWT.LEFT);

		checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION);
		checkboxTableViewer.addCheckStateListener(listener -> {
			Object[] elements = checkboxTableViewer.getCheckedElements();
			if (elements.length == 0)
				okButton.setEnabled(false);
			else
				okButton.setEnabled(true);
			selectedElements = Arrays.asList(elements);
		});
		table = checkboxTableViewer.getTable();
		FormData fd_table = new FormData();
		fd_table.right = new FormAttachment(button, -6);
		fd_table.bottom = new FormAttachment(label, 260, SWT.BOTTOM);
		fd_table.top = new FormAttachment(label, 6);
		fd_table.left = new FormAttachment(label, 0, SWT.LEFT);
		table.setLayoutData(fd_table);
		checkboxTableViewer.setContentProvider(new ArrayContentProvider());
		composite_2.setLayoutData(fd_composite_2);

		Group group = new Group(composite_2, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.left = new FormAttachment(0, 10);
		fd_group.top = new FormAttachment(0);
		fd_group.bottom = new FormAttachment(0, 103);
		fd_group.right = new FormAttachment(0, 460);
		group.setLayoutData(fd_group);
		group.setText("Настройки");
		group.setLayout(new FormLayout());

		showDeletedButton = new Button(group, SWT.CHECK);
		FormData fd_showDeletedButton = new FormData();
		fd_showDeletedButton.top = new FormAttachment(0, 10);
		fd_showDeletedButton.left = new FormAttachment(0, 10);
		showDeletedButton.setLayoutData(fd_showDeletedButton);
		showDeletedButton.setText("Показывать удалённые");

		saveInFileButton = new Button(group, SWT.CHECK);
		saveInFileButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (saveInFileButton.getSelection()) {
					pathText.setEnabled(true);
					selectFolderButton.setEnabled(true);
					saveToFile=true;
				} else {
					pathText.setEnabled(false);
					selectFolderButton.setEnabled(false);
					saveToFile=false;
				}
			}
		});
		FormData fd_saveInFileButton = new FormData();
		fd_saveInFileButton.top = new FormAttachment(showDeletedButton, 6);
		fd_saveInFileButton.left = new FormAttachment(showDeletedButton, 0, SWT.LEFT);
		saveInFileButton.setLayoutData(fd_saveInFileButton);
		saveInFileButton.setText("Сохранять в файл");

		Label label_1 = new Label(group, SWT.NONE);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(saveInFileButton, 6);
		fd_label_1.left = new FormAttachment(showDeletedButton, 0, SWT.LEFT);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Папка:");

		pathText = new Text(group, SWT.BORDER);
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				saveFolder = pathText.getText();
			}
		});
		pathText.setEnabled(false);
		FormData fd_pathText = new FormData();
		fd_pathText.left = new FormAttachment(label_1, 6);
		fd_pathText.top = new FormAttachment(saveInFileButton, 6);
		pathText.setLayoutData(fd_pathText);

		selectFolderButton = new Button(group, SWT.NONE);
		selectFolderButton.setEnabled(false);
		selectFolderButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell.getShell());
				dialog.setFilterPath("\\");
				selectedPath = dialog.open();
				if (selectedPath != null)
					pathText.setText(selectedPath);
				else
					pathText.setText("");
			}
		});
		fd_pathText.right = new FormAttachment(100, -74);
		FormData fd_selectFolderButton = new FormData();
		fd_selectFolderButton.right = new FormAttachment(100, -7);
		fd_selectFolderButton.top = new FormAttachment(pathText, -2, SWT.TOP);
		fd_selectFolderButton.left = new FormAttachment(pathText, 6);
		selectFolderButton.setLayoutData(fd_selectFolderButton);
		selectFolderButton.setText("Выбрать");

		progressBar = new ProgressBar(composite_2, SWT.NONE);
		FormData fd_progressBar = new FormData();
		fd_progressBar.bottom = new FormAttachment(100, -10);
		fd_progressBar.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_progressBar.left = new FormAttachment(group, 0, SWT.LEFT);
		fd_progressBar.top = new FormAttachment(100, -27);
		progressBar.setLayoutData(fd_progressBar);

		arhiveNameLabel = new Label(composite_2, SWT.NONE);

		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.bottom = new FormAttachment(progressBar, -4);
		fd_lblNewLabel.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_lblNewLabel.left = new FormAttachment(group, 0, SWT.LEFT);
		arhiveNameLabel.setLayoutData(fd_lblNewLabel);
		setPgVisible(false);
		comboViewer.setInput(readHandler.getPorts());
		return area;
	}

	public void setPgVisible(boolean val) {
		arhiveNameLabel.setVisible(val);
		progressBar.setVisible(val);
	}

	private void readArchiveList(String port) {
		List<Archive7KTC> list = readHandler.readArchives(port, this, showDeletedButton.getSelection());
		checkboxTableViewer.setInput(list);
	}

	public void setProgressBarMaximum(int max) {
		progressBar.setSelection(0);
		arhiveNameLabel.setText("");
		pbSelection = 1;
		progressBar.setMaximum(max);
	}

	public void updateProgressBar(String text) {
		progressBar.setSelection(++pbSelection);
		arhiveNameLabel.setText(text);
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
		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		cancelButton.setText("Отменить");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(476, 652);
	}

	public Label getArhiveNameLabel() {
		return arhiveNameLabel;
	}

	public void setArhiveNameLabel(Label arhiveNameLabel) {
		this.arhiveNameLabel = arhiveNameLabel;
	}

	public CheckboxTableViewer getCheckboxTableViewer() {
		return checkboxTableViewer;
	}

	public void setCheckboxTableViewer(CheckboxTableViewer checkboxTableViewer) {
		this.checkboxTableViewer = checkboxTableViewer;
	}

	public List<Object> getSelectedElements() {
		return selectedElements;
	}

	public void setSelectedElements(List<Object> selectedElements) {
		this.selectedElements = selectedElements;
	}

	public String getSaveFolder() {
		return saveFolder;
	}

	public void setSaveFolder(String saveFolder) {
		this.saveFolder = saveFolder;
	}

	public boolean isSaveToFile() {
		return saveToFile;
	}

	public void setSaveToFile(boolean saveToFile) {
		this.saveToFile = saveToFile;
	}

	public ComboViewer getComboViewer() {
		return comboViewer;
	}

	public void setComboViewer(ComboViewer comboViewer) {
		this.comboViewer = comboViewer;
	}

	public String getComPort() {
		return comPort;
	}

	public void setComPort(String comPort) {
		this.comPort = comPort;
	}

}
