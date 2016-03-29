 
package ru.sevenkt.app.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AboutHandler {
	
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setText("О программе");
		messageBox.setMessage("Версия");
		messageBox.open();
	}
		
}