
package ru.sevenkt.reader.ui.views;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;

public class ComPortLogView extends AppenderBase<ILoggingEvent> {

	private Text txtTest;

	@Inject
	UISynchronize sync;

	private PatternLayoutEncoder ple;

	@Inject
	public ComPortLogView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		addAppender();
		txtTest = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		txtTest.setEditable(false);
	}

	private void addAppender() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ple = new PatternLayoutEncoder();

		ple.setPattern("%d{HH:mm:ss} %level %logger{10} %msg%n");
		ple.setContext(lc);

		ple.start();

		Logger loggerTX = lc.getLogger("TX");
		Logger loggerRX = lc.getLogger("RX");
		Logger loggerReader = lc.getLogger("Reader");

		loggerTX.addAppender(this);
		loggerTX.setLevel(Level.DEBUG);
		loggerTX.setAdditive(false); /* set to true if root should log too */

		loggerRX.addAppender(this);
		loggerRX.setLevel(Level.DEBUG);
		loggerRX.setAdditive(false); /* set to true if root should log too */

		loggerReader.addAppender(this);
		loggerReader.setLevel(Level.DEBUG);
		loggerReader.setAdditive(true); /* set to true if root should log too */
		start();

	}

	@Override
	public void append(ILoggingEvent event) {
		String doLayout = ple.getLayout().doLayout(event);
		sync.asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!txtTest.isDisposed())
					txtTest.append(doLayout);
			}
		});

	}

}