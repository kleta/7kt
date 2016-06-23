
package ru.sevenkt.app.ui.handlers;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Report;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.reports.services.IReportService;

public class AvailableReportItems {

	@Inject
	IDBService ds;

	@AboutToShow
	public void aboutToShow(List<MMenuElement> items, EModelService modelService,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		Device device=(Device)object;
		List<Report> reports = ds.getReports(device);
		MDirectMenuItem dynamicItem = modelService.createModelElement(MDirectMenuItem.class);
		dynamicItem.setLabel("string");
		dynamicItem.setContributorURI("platform:/plugin/ru.7kt.app");
		dynamicItem.setContributionURI("bundleclass://ru.7kt.app/ru.sevenkt.app.ui.handlers.ShowReport");
		items.add(dynamicItem);

	}

	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {

	}

}