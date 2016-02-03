 
package ru.sevenkt.app.ui.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ImportFromFileHandler {
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		
		FileDialog fd = new FileDialog(parentShell, SWT.OPEN);
        fd.setText("Открыть файл с данными");
        fd.setFilterPath("C:/");
        String[] filterExt = { "*.bin"};
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
        System.out.println(selected);
        
	}
		
}