
package ru.sevenkt.app.ui.handlers;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import ru.sevenkt.db.entities.Device;

public class DeviceViewDynamicMenuConribution {

	private Device currentSelection;
	private Shell shell;
	private MDirectMenuItem openArchiveItem;
	private MDirectMenuItem propertiesItem;
	
	@PostConstruct
	public void init(){
		openArchiveItem = MMenuFactory.INSTANCE.createDirectMenuItem();
		openArchiveItem.setLabel("Просмотр архивов");
		openArchiveItem.setIconURI("platform:/plugin/ru.7kt.app/icons/table.png");

		openArchiveItem.setContributorURI("platform:/plugin/ru.7kt.app");
		openArchiveItem.setContributionURI("bundleclass://ru.7kt.app/ru.sevenkt.app.ui.handlers.OpenArchiveHandler");
		
		propertiesItem = MMenuFactory.INSTANCE.createDirectMenuItem();
		propertiesItem.setLabel("Свойства");
		propertiesItem.setIconURI("platform:/plugin/ru.7kt.app/icons/property.png");

		propertiesItem.setContributorURI("platform:/plugin/ru.7kt.app");
		propertiesItem.setContributionURI("bundleclass://ru.7kt.app/ru.sevenkt.app.ui.handlers.EditDeviceHandler");
	}

	@AboutToShow
	public void aboutToShow(List<MMenuElement> items) {
		if (currentSelection instanceof Device) {
			
			items.add(openArchiveItem);
			
			items.add(MMenuFactory.INSTANCE.createMenuSeparator());
						
			items.add(propertiesItem);
		}
	}

	@Inject
	public void select(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Device device,
			@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		currentSelection = device;
		shell = parentShell;
	}

	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {
		
	}

}