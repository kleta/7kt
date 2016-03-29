 
package ru.sevenkt.app.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

public class AboutHandler {
	
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, MApplication application) {
		Bundle oB = org.osgi.framework.FrameworkUtil.getBundle ( this.getClass() ) ;
		System.out.println ( "Bundle: " + oB ) ;
		System.out.println ( "BundleContext: " + oB.getVersion() ) ;
		MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_INFORMATION | SWT.OK);
		messageBox.setText("О программе");
		messageBox.setMessage("Версия "+oB.getVersion().toString());
		messageBox.open();
	}
		
}