package ru.sevenkt.app.ui;

 


import javax.inject.Inject;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

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