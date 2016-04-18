
package ru.sevenkt.app.ui.handlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import ru.sevenkt.db.entities.Params;
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
			startDate = startDate.minusMonths(1);
			break;
		case DAY:
			startDate = startDate.minusDays(1);
			break;
		default:
			break;
		}
		List<Measuring> measurings = dbService.findArchive(device, startDate, endDate, archiveType);
		// if(archiveType.equals(ArchiveTypes.HOUR))
		// smoothedHourMeasuring(measurings);
		Map<LocalDateTime, List<Measuring>> groupByDateTime = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		List<Parameters> parameters = new ArrayList<>();
		List<Params> params = device.getParams();
		for (Params param : params) {
			parameters.add(param.getId());
		}
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
							if (measuring.getParameter().equals(prevMonthMeasuring.getParameter())
									&& prevMonthMeasuring.getValue() <= measuring.getValue())
								tr.getValues().put(measuring.getParameter(),
										measuring.getValue() - prevMonthMeasuring.getValue());
						} else
							tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				}
				if (parameters.contains(Parameters.M1_SUB_M2)) {
					Object m1 = tr.getValues().get(Parameters.M1);
					Object m2 = tr.getValues().get(Parameters.M2);
					if (m1 != null && m2 != null)
						tr.getValues().put(Parameters.M1_SUB_M2, ((Float) m1) - ((Float) m2));
				}
				if (parameters.contains(Parameters.M3_SUB_M4)) {
					Object m1 = tr.getValues().get(Parameters.M3);
					Object m2 = tr.getValues().get(Parameters.M4);
					if (m1 != null && m2 != null)
						tr.getValues().put(Parameters.M3_SUB_M4, ((Float) m1) - ((Float) m2));
				}
				startDateTime = startDateTime.plusMonths(1);
				break;
			case DAY:
				List<Measuring> lmPrevDay = groupByDateTime.get(startDateTime.minusDays(1));
				if (lmPrevDay != null && lm != null) {
					for (int i = 0; i < lm.size(); i++) {

						Measuring measuring = lm.get(i);

						LocalDate localDate = measuring.getDateTime().toLocalDate();
						if (localDate.equals(LocalDate.of(2016, 2, 3)))
							System.out.println();
						String category = measuring.getParameter().getCategory();
						if (!category.equals(ParametersConst.TEMP) && !category.equals(ParametersConst.PRESSURE)) {
							Measuring prevDayMeasuring = lmPrevDay.get(i);
							if (measuring.getParameter().equals(prevDayMeasuring.getParameter())
									&& prevDayMeasuring.getValue() <= measuring.getValue())
								tr.getValues().put(measuring.getParameter(),
										measuring.getValue() - prevDayMeasuring.getValue());
						} else
							tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				}
				if (parameters.contains(Parameters.M1_SUB_M2)) {
					Object m1 = tr.getValues().get(Parameters.M1);
					Object m2 = tr.getValues().get(Parameters.M2);
					if (m1 != null && m2 != null)
						tr.getValues().put(Parameters.M1_SUB_M2, ((Float) m1) - ((Float) m2));
				}
				if (parameters.contains(Parameters.M3_SUB_M4)) {
					Object m1 = tr.getValues().get(Parameters.M3);
					Object m2 = tr.getValues().get(Parameters.M4);
					if (m1 != null && m2 != null)
						tr.getValues().put(Parameters.M3_SUB_M4, ((Float) m1) - ((Float) m2));
				}
				startDateTime = startDateTime.plusDays(1);
				break;
			case HOUR:
				if (lm != null)
					for (Measuring measuring : lm) {
						tr.getValues().put(measuring.getParameter(), measuring.getValue());
					}
				startDateTime = startDateTime.plusHours(1);
				break;
			default:
				break;
			}
			if (parameters.contains(Parameters.V1_SUB_V2)) {
				Object v1 = tr.getValues().get(Parameters.V1);
				Object v2 = tr.getValues().get(Parameters.V2);
				if (v1 != null && v2 != null)
					tr.getValues().put(Parameters.V1_SUB_V2, ((Float) v1) - ((Float) v2));
			}
			if (parameters.contains(Parameters.V3_SUB_V4)) {
				Object v3 = tr.getValues().get(Parameters.V3);
				Object v4 = tr.getValues().get(Parameters.V4);
				if (v3 != null && v4 != null)
					tr.getValues().put(Parameters.V3_SUB_V4, ((Float) v3) - ((Float) v4));
			}
			if (parameters.contains(Parameters.T1_SUB_T2)) {
				Object t1 = tr.getValues().get(Parameters.AVG_TEMP1);
				Object t2 = tr.getValues().get(Parameters.AVG_TEMP2);
				if (t1 != null && t2 != null)
					tr.getValues().put(Parameters.T1_SUB_T2, ((Float) t1) - ((Float) t2));
			}
			if (parameters.contains(Parameters.T3_SUB_T4)) {
				Object t1 = tr.getValues().get(Parameters.AVG_TEMP3);
				Object t2 = tr.getValues().get(Parameters.AVG_TEMP4);
				if (t1 != null && t2 != null)
					tr.getValues().put(Parameters.T3_SUB_T4, ((Float) t1) - ((Float) t2));
			}
			listTableRow.add(tr);
		} while (startDateTime.isBefore(endDateTime));
		result.put(AppEventConstants.ARCHIVE_PARAMETERS, parameters);
		result.put(AppEventConstants.TABLE_ROWS, listTableRow);
		result.put(AppEventConstants.DEVICE, device);
		broker.send(AppEventConstants.TOPIC_RESPONSE_ARCHIVE, result);
	}

	private void smoothedHourMeasuring(List<Measuring> lm) {
		Map<LocalDateTime, Map<Parameters, Double>> cashMult = new HashMap<>();
		for (Measuring measuring : lm) {
			LocalDateTime dt = measuring.getDateTime();
			LocalDateTime dtFrom = null;
			LocalDateTime dtTo = null;
			if (dt.toLocalTime().equals(LocalTime.of(0, 0))) {
				dtFrom = dt.minusHours(23);
				dtTo = dt;
			} else {
				dtFrom = dt.withHour(1);
				dtTo = dtFrom.plusHours(23);
			}
			Map<Parameters, Double> mapParameter = cashMult.get(dtTo);
			Double mult;
			if (mapParameter != null) {
				mult = mapParameter.get(measuring.getParameter());
				if (mult == null) {
					mult = dbService.getSmoothedMultiplier(measuring);
					mapParameter.put(measuring.getParameter(), mult);
				}
			} else {
				mapParameter = new HashMap<>();
				cashMult.put(dtTo, mapParameter);
				mult = dbService.getSmoothedMultiplier(measuring);
				mapParameter.put(measuring.getParameter(), mult);
			}
			measuring.setValue(measuring.getValue() * mult);
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Object object)
			throws IllegalArgumentException, IllegalAccessException {

		if (object instanceof Device)
			return true;
		return false;
	}
}