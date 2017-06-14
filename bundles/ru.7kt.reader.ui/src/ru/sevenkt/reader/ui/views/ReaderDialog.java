package ru.sevenkt.reader.ui.views;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;

import ru.sevenkt.domain.ArchiveTypes;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ReaderDialog extends TitleAreaDialog {
	private Text text;
	private LocalDate to;
	private LocalDate from;
	private Button dayButton;
	private Button monthButton;
	private Button hourButton;
	private Button journalButton;
	private Button fullButton;
	private CDateTime fromDate;
	private CDateTime toDate;
	Set<ArchiveTypes> types = new HashSet<>();
	private Button buttonOK;
	protected boolean journal;
	protected boolean full;
	private Button folderSelectButton;
	protected String folderPath;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ReaderDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Выберите параметры для чтения");
		setTitle("Чтение данных с устройства");
		setTitleImage(ResourceManager.getPluginImage("ru.7kt.reader.ui", "resources/binary_64.png"));
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group group = new Group(container, SWT.NONE);
		group.setText("Период");
		group.setLayout(new FormLayout());
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		fd_group.right = new FormAttachment(0, 396);
		group.setLayoutData(fd_group);

		Label label = new Label(group, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 10);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		label.setText("Начало:");

		fromDate = new CDateTime(group, CDT.BORDER | CDT.DROP_DOWN);
		fromDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.Date d = fromDate.getSelection();
				Instant instant = Instant.ofEpochMilli(d.getTime());
				from = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
				if(from.isBefore(to))
					buttonOK.setEnabled(true);
				else
					buttonOK.setEnabled(false);
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				
			}
		});
		FormData fd_fromDate = new FormData();
		fd_fromDate.left = new FormAttachment(label, 6);
		fromDate.setLayoutData(fd_fromDate);

		toDate = new CDateTime(group, CDT.BORDER | CDT.DROP_DOWN);
		toDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				java.util.Date d = toDate.getSelection();
				Instant instant = Instant.ofEpochMilli(d.getTime());
				to = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
				if(from.isBefore(to))
					buttonOK.setEnabled(true);
				else
					buttonOK.setEnabled(false);
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
			}
		});
		fd_fromDate.top = new FormAttachment(toDate, 0, SWT.TOP);
		FormData fd_toDate = new FormData();
		fd_toDate.left = new FormAttachment(100, -86);
		fd_toDate.top = new FormAttachment(0, 2);
		fd_toDate.right = new FormAttachment(100, -7);
		toDate.setLayoutData(fd_toDate);

		Label label_1 = new Label(group, SWT.NONE);
		fd_fromDate.right = new FormAttachment(label_1, -89);
		FormData fd_label_1 = new FormData();
		fd_label_1.top = new FormAttachment(0, 10);
		fd_label_1.right = new FormAttachment(toDate, -9);
		label_1.setLayoutData(fd_label_1);
		label_1.setText("Окончание:");

		Group group_1 = new Group(container, SWT.NONE);
		fd_group.bottom = new FormAttachment(100, -238);
		group_1.setText("Параметры чтения");
		FormData fd_group_1 = new FormData();
		fd_group_1.top = new FormAttachment(group, 6);
		fd_group_1.left = new FormAttachment(0, 10);
		fd_group_1.right = new FormAttachment(0, 396);
		group_1.setLayoutData(fd_group_1);

		monthButton = new Button(group_1, SWT.CHECK);
		monthButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (monthButton.getSelection())
					types.add(ArchiveTypes.MONTH);
				else
					types.remove(ArchiveTypes.MONTH);
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				else
					buttonOK.setEnabled(true);
			}
		});
		monthButton.setBounds(10, 22, 100, 16);
		monthButton.setText("Месячный архив");

		dayButton = new Button(group_1, SWT.CHECK);
		dayButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dayButton.getSelection())
					types.add(ArchiveTypes.DAY);
				else
					types.remove(ArchiveTypes.DAY);
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				else
					buttonOK.setEnabled(true);
			}
		});
		dayButton.setBounds(10, 44, 96, 16);
		dayButton.setText("Суточный архив");

		hourButton = new Button(group_1, SWT.CHECK);
		hourButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (hourButton.getSelection())
					types.add(ArchiveTypes.HOUR);
				else
					types.remove(ArchiveTypes.HOUR);
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				else
					buttonOK.setEnabled(true);
			}
		});
		hourButton.setBounds(10, 66, 91, 16);
		hourButton.setText("Часовой архив");

		journalButton = new Button(group_1, SWT.CHECK);
		journalButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				else
					buttonOK.setEnabled(true);
				if(journalButton.getSelection())
					journal=true;
				else 
					journal=false;
			}
		});
		journalButton.setBounds(10, 88, 109, 16);
		journalButton.setText("Журнал установок");

		Group group_2 = new Group(container, SWT.NONE);
		fd_group_1.bottom = new FormAttachment(100, -118);
		group_2.setText("Настройки");
		group_2.setLayout(new FormLayout());
		FormData fd_group_2 = new FormData();
		fd_group_2.top = new FormAttachment(group_1, 6);

		fullButton = new Button(group_1, SWT.CHECK);
		fullButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fullButton.getSelection()) {
					fromDate.setEnabled(false);
					toDate.setEnabled(false);
					monthButton.setEnabled(false);
					dayButton.setEnabled(false);
					hourButton.setEnabled(false);
					journalButton.setEnabled(false);
				} else {
					fromDate.setEnabled(true);
					toDate.setEnabled(true);
					monthButton.setEnabled(true);
					dayButton.setEnabled(true);
					hourButton.setEnabled(true);
					journalButton.setEnabled(true);
				}
				if(types.size()==0 && !journalButton.getSelection() && !fullButton.getSelection())
					buttonOK.setEnabled(false);
				else
					buttonOK.setEnabled(true);
				if(fullButton.getSelection())
					full=true;
				else
					full=false;
				
			}
		});
		fullButton.setBounds(200, 22, 100, 16);
		fullButton.setText("Полный архив");
		fd_group_2.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_group_2.left = new FormAttachment(0, 10);
		fd_group_2.bottom = new FormAttachment(100, -21);
		group_2.setLayoutData(fd_group_2);

		Button button_5 = new Button(group_2, SWT.CHECK);
		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(button_5.getSelection()){
					folderSelectButton.setEnabled(true);
					folderPath=text.getText();
					text.setEnabled(true);
				}
				else{
					folderSelectButton.setEnabled(true);
					text.setEnabled(false);
					folderPath=null;
				}
					
			}
		});
		button_5.setText("Сохранять в файл");
		FormData fd_button_5 = new FormData();
		fd_button_5.left = new FormAttachment(0, 10);
		fd_button_5.top = new FormAttachment(0, 10);
		button_5.setLayoutData(fd_button_5);

		Label label_2 = new Label(group_2, SWT.NONE);
		label_2.setText("Папка:");
		FormData fd_label_2 = new FormData();
		fd_label_2.top = new FormAttachment(button_5, 6);
		fd_label_2.left = new FormAttachment(button_5, 0, SWT.LEFT);
		label_2.setLayoutData(fd_label_2);

		text = new Text(group_2, SWT.BORDER);
		text.setEnabled(false);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(label_2, 6);
		fd_text.top = new FormAttachment(label_2, -3, SWT.TOP);
		text.setLayoutData(fd_text);

		folderSelectButton = new Button(group_2, SWT.NONE);
		folderSelectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				dialog.setFilterPath("\\");
				folderPath = dialog.open();
				if (folderPath != null)
					text.setText(folderPath);
				else
					text.setText("");
			}
		});
		fd_text.right = new FormAttachment(folderSelectButton, -6);
		folderSelectButton.setEnabled(false);
		folderSelectButton.setText("Выбрать");
		FormData fd_folderSelectButton = new FormData();
		fd_folderSelectButton.top = new FormAttachment(label_2, -5, SWT.TOP);
		fd_folderSelectButton.right = new FormAttachment(100, -10);
		fd_folderSelectButton.left = new FormAttachment(0, 318);
		folderSelectButton.setLayoutData(fd_folderSelectButton);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				folderPath = text.getText();
			}
		});
		initState();
		return area;
	}

	private void initState() {
		to = LocalDate.now();
		from = to.minusMonths(1);
		fromDate.setSelection(Date.from(from.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		toDate.setSelection(Date.from(to.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		types.add(ArchiveTypes.DAY);
		types.add(ArchiveTypes.HOUR);
		dayButton.setSelection(true);
		hourButton.setSelection(true);
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		buttonOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(412, 431);
	}

	public LocalDate getTo() {
		return to;
	}

	public void setTo(LocalDate to) {
		this.to = to;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public boolean isJournal() {
		return journal;
	}

	public void setJournal(boolean journal) {
		this.journal = journal;
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Set<ArchiveTypes> getTypes() {
		return types;
	}

	public void setTypes(Set<ArchiveTypes> types) {
		this.types = types;
	}
}
