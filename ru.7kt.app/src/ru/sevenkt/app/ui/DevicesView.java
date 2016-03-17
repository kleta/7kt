
package ru.sevenkt.app.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.ResourceManager;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.services.IDBService;

public class DevicesView {

	@Inject
	private IDBService dbService;

	@Inject
	ESelectionService selectionService;

	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			if (element.equals("Устройства")) {
				return ResourceManager.getPluginImage("ru.7kt.app", "icons/folder.png");
			}
			if (element instanceof Device)
				return ResourceManager.getPluginImage("ru.7kt.app", "icons/pda.png");
			return super.getImage(element);
		}

		public String getText(Object element) {
			String className = element.getClass().getName();
			switch (className) {
			case "ru.sevenkt.db.entities.Device":
				Device device = (Device) element;
				return device.getDeviceName() + " №" + device.getSerialNum();

			default:
				break;
			}
			return super.getText(element);
		}
	}

	private static class TreeContentProvider implements ITreeContentProvider {
		private IDBService dbService;

		public TreeContentProvider(IDBService dbService) {
			this.dbService = dbService;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			System.out.println();
		}

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement.equals("")) {
				return new Object[] { "Устройства" };
			}
			return getChildren(inputElement);
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement.equals("Устройства")) {
				return dbService.findAllDevices().toArray(new Device[dbService.findAllDevices().size()]);
			}
			return new Object[] {};
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	private TreeViewer treeViewer;

	@Inject
	public DevicesView() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, ESelectionService s, EMenuService ms) {
		treeViewer = new TreeViewer(parent, SWT.BORDER);
		treeViewer.setLabelProvider(new ViewerLabelProvider());
		treeViewer.setContentProvider(new TreeContentProvider(dbService));
		ms.registerContextMenu(treeViewer.getControl(), 
		        "ru.7kt.app.popupmenu.device");
		treeViewer.addSelectionChangedListener(listener -> {
			IStructuredSelection selection = treeViewer.getStructuredSelection();
			s.setSelection(selection.getFirstElement());
		});
		treeViewer.setInput("");
		treeViewer.expandToLevel(2);
	}

}