 
package ru.sevenkt.app.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;

public class OutputView {
	private Text text;
	@Inject
	public OutputView() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		
		text = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		
	}
	
	
	
	
}