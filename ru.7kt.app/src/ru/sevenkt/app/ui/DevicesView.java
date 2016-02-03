
package ru.sevenkt.app.ui;

import javax.inject.Inject;
import javax.annotation.PostConstruct;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

public class DevicesView {

	@Inject
	public DevicesView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		
		TreeViewer treeViewer = new TreeViewer(parent, SWT.BORDER);
		Tree tree = treeViewer.getTree();

	}

}