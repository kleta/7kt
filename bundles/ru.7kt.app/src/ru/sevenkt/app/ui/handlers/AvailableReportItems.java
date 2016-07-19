
package ru.sevenkt.app.ui.handlers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.ui.di.AboutToShow;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
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
	public static final String COMMAND_ID = "ru.7kt.app.command.report";
	
	public static final String COMMAND_PARAMETER_ID = "ru.7kt.app.commandparameter.report";

	@Inject
	IDBService ds;

	@Inject
	protected MApplication app;

	@AboutToShow
	public void aboutToShow(List<MMenuElement> items, EModelService modelService,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object) {
		Device device = (Device) object;
		List<Report> reports = ds.getReports(device);
		for (Report report : reports) {
			MHandledMenuItem dynamicItem = modelService.createModelElement(MHandledMenuItem.class);
			dynamicItem.setLabel(report.getName());
			MCommand command = getCommand(COMMAND_ID); 

			dynamicItem.setContributorURI("platform:/plugin/ru.7kt.app");
			dynamicItem.setCommand(command);
			MParameter parameter = MCommandsFactory.INSTANCE.createParameter(); 
			dynamicItem.setIconURI("platform:/plugin/ru.7kt.app/icons/report.png");
		    parameter.setName(COMMAND_PARAMETER_ID); 
		    parameter.setValue(report.getId()+"");	
		    List<MParameter> parameters = dynamicItem.getParameters();
			parameters.add(parameter);
			items.add(dynamicItem);
		}

	}

	@AboutToHide
	public void aboutToHide(List<MMenuElement> items) {

	}

	public MCommand getCommand(String elementId) {
		List<MCommand> commands = app.getCommands();

		for (MCommand command : commands) {
			if (command.getElementId().equals(elementId)) {
				return command;
			}
		}

		return null;
	}

}