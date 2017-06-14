package ru.sevenkt.app.ui.forms;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Button;

public class AddDeviceDialog extends TitleAreaDialog {
	private Text text;
	private Text text_1;
	protected int serialNum;
	protected String name;
	private Button buttonOK;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public AddDeviceDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Введите наименование и серийный номер тепловычислителя");
		setTitle("Новое устройство");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 13);
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Наименование:");
		
		text = new Text(container, SWT.BORDER);
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Text text = (Text)e.getSource();
	            name = text.getText();
	            if (name.equals(""))
	            	buttonOK.setEnabled(false);
	            else
	            	buttonOK.setEnabled(true);
			}
		});
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				Text text = (Text)e.getSource();

	            // get old text and create new text by using the VerifyEvent.text
	            final String oldS = text.getText();
	            String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

	            boolean isInt = true;
	            try
	            {
	                serialNum = Integer.parseInt(newS);
	            }
	            catch(NumberFormatException ex)
	            {
	                isInt = false;
	            }

	            System.out.println(newS);

	            if(!isInt){
	                e.doit = false;
	                buttonOK.setEnabled(false);
	            }
			}
		});
		fd_text.right = new FormAttachment(text_1, 0, SWT.RIGHT);
		fd_text.left = new FormAttachment(text_1, 0, SWT.LEFT);
		FormData fd_text_1 = new FormData();
		fd_text_1.top = new FormAttachment(text, 13);
		fd_text_1.right = new FormAttachment(100, -10);
		text_1.setLayoutData(fd_text_1);
		
		Label label = new Label(container, SWT.NONE);
		fd_text_1.left = new FormAttachment(label, 6);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(lblNewLabel, 19);
		fd_label.left = new FormAttachment(lblNewLabel, 0, SWT.LEFT);
		label.setLayoutData(fd_label);
		label.setText("Серийный номер:");

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		buttonOK = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		buttonOK.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(399, 235);
	}

	public int getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Новое устройство");
	}
	
}
