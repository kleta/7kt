package ru.sevenkt.app.ui;

 


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class OutputView{
	private Text text;
	@Inject
	public OutputView() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {	
//		Logger l = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("");
//		List<Logger> app = l.getLoggerContext().getLoggerList();
//		System.out.println(l.getClass());
		text = new Text(parent, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);	
	}	
}