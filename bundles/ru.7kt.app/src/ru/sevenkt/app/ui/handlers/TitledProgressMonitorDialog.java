package ru.sevenkt.app.ui.handlers;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

public class TitledProgressMonitorDialog extends ProgressMonitorDialog {
	
	private String title;

	public TitledProgressMonitorDialog(Shell parent, String title) {
		super(parent);
		this.title=title;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(title);
	}

	

}
