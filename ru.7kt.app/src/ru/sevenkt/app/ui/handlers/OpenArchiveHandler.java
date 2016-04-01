
package ru.sevenkt.app.ui.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.text.TableView.TableCell;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import ru.sevenkt.app.ui.TableRow;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class OpenArchiveHandler implements EventHandler {

	@Inject
	private IEventBroker broker;

	@Inject
	private IDBService dbService;

	@PostConstruct
	public void lazyLoadRequest() {
		broker.subscribe(AppEventConstants.TOPIC_REQUEST_ARCHIVE, this);
	}

	@Execute
	public void execute(EPartService partService, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object,
			EModelService modelService, MApplication application) {

		if (object != null) {
			if (object instanceof Device) {
				Device device = (Device) object;
				MPart part = (MPart) modelService.find("ru.7kt.app.partdescriptor.arciveView_" + device.getId(),
						application);
				if (part == null) {
					MPartStack stack = (MPartStack) modelService.find("ru.7kt.partstack.centr", application);
					part = partService.createPart("ru.7kt.app.partdescriptor.arciveView");
					part.setLabel(device.getDeviceName() + " №" + device.getSerialNum());
					part.setElementId(part.getElementId() + "_" + device.getId());
					stack.getChildren().add(part);
				}
				partService.showPart(part, PartState.ACTIVATE);
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		ArchiveTypes archiveType = (ArchiveTypes) event.getProperty(AppEventConstants.ARCHIVE_TYPE);
		Device device = (Device) event.getProperty(AppEventConstants.DEVICE);
		LocalDate startDate = (LocalDate) event.getProperty(AppEventConstants.START_DATE);
		LocalDateTime startDateTime = startDate.atStartOfDay();
		if (archiveType.equals(ArchiveTypes.MONTH) && startDateTime.getDayOfMonth() != 1)
			startDateTime = startDateTime.plusMonths(1).withDayOfMonth(1);
		LocalDate endDate = (LocalDate) event.getProperty(AppEventConstants.END_DATE);
		LocalDateTime endDateTime = endDate.atStartOfDay();

		Map<String, Object> result = new HashMap<>();
		switch (archiveType) {
		case MONTH:
			startDate=startDate.minusMonths(1);
			break;
		case DAY:
			startDate=startDate.minusDays(1);
			break;
		default:
			break;
		}
		List<Measuring> measurings = dbService.findArchive(device, startDate, endDate, archiveType);
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		Map<Parameters, List<Measuring>> groupByParameters = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getParameter));
		List<Parameters> parameters = new ArrayList<>();
		groupByParameters.keySet().iterator().forEachRemaining(parameters::add);
		List<TableRow> listTableRow = new ArrayList<>();
		do {
			TableRow tr = new TableRow();
			tr.setDateTime(startDateTime);
			List<Measuring> lm = groupByDateTime.get(startDateTime);
			switch (archiveType) {
			case MONTH:
				List<Measuring> lmPrevMonth = groupByDateTime.get(startDateTime.minusMonths(1));
				if (lmPrevMonth != null && lm != null) {
					for (int i = 0; i < lmPrevMonth.size(); i++) {
						Measuring measuring = lm.get(i);
						String category = measuring.getParameter().getCategory();
						if (!category.equals(ParametersConst.TEMP) && !category.equals(ParametersConst.PRESSURE)) {
							Measuring prevMonthMeasuring = lmPrevMonth.get(i);
							if(measuring.getParameter().equals(prevMonthMeasuring.getParameter()) && prevMonthMeasuring.getValue()<=measuring.getValue())
								tr.getValues().put(measuring.getParameter(), measuring.getValue()-prevMonthMeasuring.getValue());
						}
						else
							tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				}
				listTableRow.add(tr);
				startDateTime = startDateTime.plusMonths(1);
				break;
			case DAY:
				List<Measuring> lmPrevDay = groupByDateTime.get(startDateTime.minusDays(1));
				if (lmPrevDay != null && lm != null) {
					for (int i = 0; i < lmPrevDay.size(); i++) {
						Measuring measuring = lm.get(i);
						String category = measuring.getParameter().getCategory();
						if (!category.equals(ParametersConst.TEMP) && !category.equals(ParametersConst.PRESSURE)) {
							Measuring prevDayMeasuring = lmPrevDay.get(i);
							if(measuring.getParameter().equals(prevDayMeasuring.getParameter()) && prevDayMeasuring.getValue()<=measuring.getValue())
								tr.getValues().put(measuring.getParameter(), measuring.getValue()-prevDayMeasuring.getValue());
						}
						else
							tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				}
				listTableRow.add(tr);
				startDateTime = startDateTime.plusDays(1);
				break;
			case HOUR:
				if (lm != null)
					for (Measuring measuring : lm) {
						tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				listTableRow.add(tr);
				startDateTime = startDateTime.plusHours(1);
				break;
			default:
				break;
			}
		} while (startDateTime.isBefore(endDateTime));
		result.put(AppEventConstants.ARCHIVE_PARAMETERS, parameters);
		result.put(AppEventConstants.TABLE_ROWS, listTableRow);
		result.put(AppEventConstants.DEVICE, device);
		broker.send(AppEventConstants.TOPIC_RESPONSE_ARCHIVE, result);
	}
	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object)
			throws IllegalArgumentException, IllegalAccessException {

		if (object instanceof Device)
			return true;
		return false;

	}
}