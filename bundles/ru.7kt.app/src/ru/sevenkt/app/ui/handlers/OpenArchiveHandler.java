
package ru.sevenkt.app.ui.handlers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

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

import ru.sevenkt.app.ui.DateTimeTableRow;
import ru.sevenkt.app.ui.SumTableRow;
import ru.sevenkt.app.ui.TableRow;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.Params;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.ErrorCodes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class OpenArchiveHandler implements EventHandler {

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

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
		// if (archiveType.equals(ArchiveTypes.MONTH) &&
		// startDateTime.getDayOfMonth() != 1)
		// startDateTime = startDateTime.plusMonths(1).withDayOfMonth(1);
		LocalDate endDate = (LocalDate) event.getProperty(AppEventConstants.END_DATE);
		LocalDateTime endDateTime = endDate.atStartOfDay();
		Map<String, Object> result = new HashMap<>();
		switch (archiveType) {
		// case HOUR:
		// startDateTime = startDateTime.plusHours(1);
		//// endDateTime=endDateTime.plusHours(1);
		// break;
		case MONTH:
			startDateTime = startDateTime.withDayOfMonth(1);
			endDateTime=endDateTime.minusDays(1);
			break;
		// case DAY:
		// startDateTime = startDateTime.withHour(0);
		// break;
		default:
			startDateTime = startDateTime.withHour(0);
			break;
		}
		List<Measuring> measurings = dbService.findArchive(device, startDateTime, endDateTime, archiveType);
		List<Error> errors = dbService.findErrors(device, startDate.atStartOfDay(), endDate.atStartOfDay(),
				archiveType);
		Map<LocalDateTime, List<Measuring>> groupByDateTimeMeasurings = measurings.stream()
				.collect(Collectors.groupingBy(Measuring::getDateTime));
		Map<LocalDateTime, List<Error>> groupByDateTimeErrors = errors.stream()
				.collect(Collectors.groupingBy(Error::getDateTime));
		List<Parameters> parameters = new ArrayList<>();
		List<Params> params = device.getParams();
		for (Params param : params) {
			parameters.add(param.getId());
		}
		List<TableRow> listTableRow = new ArrayList<>();
		while (startDateTime.isBefore(endDateTime)) {
			TableRow tr = new DateTimeTableRow();
			List<Measuring> lm = null;
			List<Error> le = null;
			int hoursInArchivePeriod = 1;
			switch (archiveType) {
			case MONTH:
				hoursInArchivePeriod = (int) ChronoUnit.HOURS.between(startDateTime, startDateTime.plusMonths(1));
				startDateTime = startDateTime.plusMonths(1);
				if (startDateTime.isAfter(endDateTime))
					break;
				tr.setFirstColumn(startDateTime.format(formatter));
				lm = groupByDateTimeMeasurings.get(startDateTime);
				le = groupByDateTimeErrors.get(startDateTime);
				parameters=parameters.stream().filter(p->!p.getCategory().equals(ParametersConst.TEMP))
						.filter(p->!p.getCategory().equals(ParametersConst.PRESSURE))
						.filter(p->!p.equals(Parameters.T1_SUB_T2))
						.filter(p->!p.equals(Parameters.T3_SUB_T4)).collect(Collectors.toList());
				addMonthColumns(startDateTime, groupByDateTimeMeasurings, parameters, tr, lm);
				break;
			case DAY:
				hoursInArchivePeriod = (int) ChronoUnit.HOURS.between(startDateTime, startDateTime.plusDays(1));
				startDateTime = startDateTime.plusDays(1);
				tr.setFirstColumn(startDateTime.format(formatter));
				lm = groupByDateTimeMeasurings.get(startDateTime);
				le = groupByDateTimeErrors.get(startDateTime);
				addDayColumns(startDateTime, groupByDateTimeMeasurings, parameters, tr, lm);				
				break;
			case HOUR:
				startDateTime = startDateTime.plusHours(1);
				tr.setFirstColumn(startDateTime.format(formatter));
				lm = groupByDateTimeMeasurings.get(startDateTime);
				le = groupByDateTimeErrors.get(startDateTime);
				parameters=parameters.stream().filter(p->!p.getCategory().equals(ParametersConst.WEIGHT))
						.filter(p->!p.equals(Parameters.M1_SUB_M2))
						.filter(p->!p.equals(Parameters.M3_SUB_M4))
						.collect(Collectors.toList());
				addHourColumns(parameters, tr, lm);
				break;
			default:
				break;
			}
			if (parameters.contains(Parameters.V1_SUB_V2)) {
				BigDecimal v1 = (BigDecimal) tr.getValues().get(Parameters.V1);
				BigDecimal v2 = (BigDecimal) tr.getValues().get(Parameters.V2);
				if (v1 != null && v2 != null)
					tr.getValues().put(Parameters.V1_SUB_V2, v1.subtract(v2));
			}
			if (parameters.contains(Parameters.V3_SUB_V4)) {
				BigDecimal v3 = (BigDecimal) tr.getValues().get(Parameters.V3);
				BigDecimal v4 = (BigDecimal) tr.getValues().get(Parameters.V4);
				if (v3 != null && v4 != null)
					tr.getValues().put(Parameters.V3_SUB_V4, v3.subtract(v4));
			}
			if (parameters.contains(Parameters.T1_SUB_T2)) {
				BigDecimal t1 = (BigDecimal) tr.getValues().get(Parameters.AVG_TEMP1);
				BigDecimal t2 = (BigDecimal) tr.getValues().get(Parameters.AVG_TEMP2);
				if (t1 != null && t2 != null)
					tr.getValues().put(Parameters.T1_SUB_T2, t1.subtract(t2));
			}
			if (parameters.contains(Parameters.T3_SUB_T4)) {
				BigDecimal t1 = (BigDecimal) tr.getValues().get(Parameters.AVG_TEMP3);
				BigDecimal t2 = (BigDecimal) tr.getValues().get(Parameters.AVG_TEMP4);
				if (t1 != null && t2 != null)
					tr.getValues().put(Parameters.T3_SUB_T4, t1.subtract(t2));
			}
			if (parameters.contains(Parameters.NO_ERROR_TIME1)) {
				BigDecimal t1 = (BigDecimal) tr.getValues().get(Parameters.ERROR_TIME1);
				if (t1 != null)
					tr.getValues().put(Parameters.NO_ERROR_TIME1, new BigDecimal(hoursInArchivePeriod - t1.intValue()));
			}
			if (parameters.contains(Parameters.NO_ERROR_TIME2)) {
				BigDecimal t1 = (BigDecimal) tr.getValues().get(Parameters.ERROR_TIME1);
				if (t1 != null)
					tr.getValues().put(Parameters.NO_ERROR_TIME2, new BigDecimal(hoursInArchivePeriod - t1.intValue()));
			}	
			if (tr.getFirstColumn() != null){
				addErrorColumns(tr, le);
				listTableRow.add(tr);
			}
		}
		addSumRow(listTableRow, parameters);
		if(!archiveType.equals(ArchiveTypes.MONTH))
			addAvgRow(listTableRow, parameters);
		result.put(AppEventConstants.ARCHIVE_PARAMETERS, parameters);
		result.put(AppEventConstants.TABLE_ROWS, listTableRow);
		result.put(AppEventConstants.DEVICE, device);
		broker.send(AppEventConstants.TOPIC_RESPONSE_ARCHIVE, result);
	}

	private void addAvgRow(List<TableRow> listTableRow, List<Parameters> parameters) {

		TableRow trAvg = new AvgTableRow();
		trAvg.setFirstColumn("СРЕДНЕЕ:");
		for (Parameters parameter : parameters) {
			if (parameter.equals(Parameters.AVG_TEMP1) || parameter.equals(Parameters.AVG_TEMP2)
					|| parameter.equals(Parameters.AVG_TEMP3) || parameter.equals(Parameters.AVG_TEMP4)
					|| parameter.equals(Parameters.AVG_P1) || parameter.equals(Parameters.AVG_P2)
					|| parameter.equals(Parameters.T1_SUB_T2) || parameter.equals(Parameters.T3_SUB_T4)) {
				BigDecimal val = new BigDecimal("0");
				int count = 0;
				for (TableRow tr : listTableRow) {
					Object value = tr.getValues().get(parameter);
					if (value != null && value instanceof BigDecimal) {		
						val = val.add((BigDecimal) value);
						count++;
					}
				}
				if (count != 0)
					val = val.divide(new BigDecimal(count+""),2, BigDecimal.ROUND_HALF_UP);
				trAvg.getValues().put(parameter, val);
			} else {
				trAvg.getValues().put(parameter, "");
			}
		}
		listTableRow.add(trAvg);

	}

	private void addSumRow(List<TableRow> listTableRow, List<Parameters> parameters) {
		TableRow trSum = new SumTableRow();
		trSum.setFirstColumn("ИТОГО:");
		for (Parameters parameter : parameters) {
			if (!parameter.equals(Parameters.AVG_TEMP1) && !parameter.equals(Parameters.AVG_TEMP2)
					&& !parameter.equals(Parameters.AVG_TEMP3) && !parameter.equals(Parameters.AVG_TEMP4)
					&& !parameter.equals(Parameters.AVG_P1) && !parameter.equals(Parameters.AVG_P2)
					&& !parameter.equals(Parameters.T1_SUB_T2) && !parameter.equals(Parameters.T3_SUB_T4)
					&& !parameter.equals(Parameters.ERROR_CODE1) && !parameter.equals(Parameters.ERROR_CODE2)) {
				BigDecimal val = new BigDecimal(""+0);
				for (TableRow tr : listTableRow) {
					Map<Parameters, Object> values = tr.getValues();
					if(values.get(parameter) instanceof Double)
						System.out.println();
					BigDecimal object = (BigDecimal) values.get(parameter);
					if (object != null)
						val = val.add(object);
				}
				//val = new BigDecimal(val + "").setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				trSum.getValues().put(parameter, val);
			} else {
				trSum.getValues().put(parameter, "");
			}
		}
		if (trSum != null)
			listTableRow.add(trSum);

	}

	private void addHourColumns(List<Parameters> parameters, TableRow tr, List<Measuring> lm) {
		if (lm != null)
			for (Measuring measuring : lm) {
				if (!parameters.equals(Parameters.ERROR_BYTE1) && !parameters.equals(Parameters.ERROR_BYTE2))
					tr.getValues().put(measuring.getParameter(), measuring.getValue());
			}
	}

	private void addDayColumns(LocalDateTime startDateTime,
			Map<LocalDateTime, List<Measuring>> groupByDateTimeMeasurings, List<Parameters> parameters, TableRow tr,
			List<Measuring> lm) {
		
		List<Measuring> lmPrevDay = groupByDateTimeMeasurings.get(startDateTime.minusDays(1));
		if (lmPrevDay != null && lm != null) {
			Map<Parameters, BigDecimal> lmMap = lm.stream().collect(Collectors.toMap(Measuring::getParameter, Measuring::getValue));
			for (Parameters parameter:lmMap.keySet()) {
				Map<Parameters, BigDecimal> lmPrevMap = lmPrevDay.stream().collect(Collectors.toMap(Measuring::getParameter, Measuring::getValue));
				BigDecimal val = lmMap.get(parameter);
				BigDecimal prevVal = lmPrevMap.get(parameter);
				if(prevVal==null)
					System.out.println();
				String category = parameter.getCategory();
				if (!category.equals(ParametersConst.TEMP) && !category.equals(ParametersConst.PRESSURE)
						&& !category.equals(ParametersConst.TIME)) {				
					if (prevVal.doubleValue() <= val.doubleValue())
						tr.getValues().put(parameter, val.subtract(prevVal));
				} else if (!parameter.equals(Parameters.ERROR_BYTE1) && !parameter.equals(Parameters.ERROR_BYTE2))
					tr.getValues().put(parameter,val);
			}
		}
		if (parameters.contains(Parameters.M1_SUB_M2)) {
			BigDecimal m1 = (BigDecimal) tr.getValues().get(Parameters.M1);
			BigDecimal m2 = (BigDecimal) tr.getValues().get(Parameters.M2);
			if (m1 != null && m2 != null)
				tr.getValues().put(Parameters.M1_SUB_M2, m1.subtract(m2));
		}
		if (parameters.contains(Parameters.M3_SUB_M4)) {
			BigDecimal m1 = (BigDecimal) tr.getValues().get(Parameters.M3);
			BigDecimal m2 = (BigDecimal) tr.getValues().get(Parameters.M4);
			if (m1 != null && m2 != null)
				tr.getValues().put(Parameters.M3_SUB_M4, m1.subtract(m2));
		}
	}

	private void addMonthColumns(LocalDateTime startDateTime,
			Map<LocalDateTime, List<Measuring>> groupByDateTimeMeasurings, List<Parameters> parameters, TableRow tr,
			List<Measuring> lm) {
		List<Measuring> lmPrevMonth = groupByDateTimeMeasurings.get(startDateTime.minusMonths(1));
		if (lmPrevMonth != null && lm != null) {
			for (int i = 0; i < lmPrevMonth.size(); i++) {
				System.out.println(i);
				Measuring measuring = lm.get(i);
				String category = measuring.getParameter().getCategory();
				if (!category.equals(ParametersConst.TEMP) && !category.equals(ParametersConst.PRESSURE)
						&& !category.equals(ParametersConst.TIME)) {
					Measuring prevMonthMeasuring = lmPrevMonth.get(i);
					if (measuring.getParameter().equals(prevMonthMeasuring.getParameter())
							&& prevMonthMeasuring.getValue().doubleValue() <= measuring.getValue().doubleValue())
						tr.getValues().put(measuring.getParameter(),
								measuring.getValue().subtract(prevMonthMeasuring.getValue()));
				} else if (!parameters.equals(Parameters.ERROR_BYTE1) && !parameters.equals(Parameters.ERROR_BYTE2))
					tr.getValues().put(measuring.getParameter(), measuring.getValue());
			}
		}
		if (parameters.contains(Parameters.M1_SUB_M2)) {
			Object m1 = tr.getValues().get(Parameters.M1);
			Object m2 = tr.getValues().get(Parameters.M2);
			if (m1 != null && m2 != null)
				tr.getValues().put(Parameters.M1_SUB_M2, ((Double) m1) - ((Double) m2));
		}
		if (parameters.contains(Parameters.M3_SUB_M4)) {
			Object m1 = tr.getValues().get(Parameters.M3);
			Object m2 = tr.getValues().get(Parameters.M4);
			if (m1 != null && m2 != null)
				tr.getValues().put(Parameters.M3_SUB_M4, ((Double) m1) - ((Double) m2));
		}
	}

	private void addErrorColumns(TableRow tr, List<Error> le) {
		if (tr instanceof DateTimeTableRow) {
			LocalDateTime dateTime = LocalDateTime.parse(tr.getFirstColumn(), formatter);
			if (le != null && !le.isEmpty()) {
				Device device = le.get(0).getDevice();
				List<Params> params = device.getParams();
				for (Error error : le) {
					ErrorCodes code = error.getErrorCode();
					Object p = null;
					switch (code) {
					case E1:
						p = tr.getValues().get(Parameters.ERROR_CODE1);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE1, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE1, p.toString() + code.getCode());
						break;
					case E2:
						p = tr.getValues().get(Parameters.ERROR_CODE2);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE2, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE2, p.toString() + code.getCode());
						break;
					case T1:
						p = tr.getValues().get(Parameters.ERROR_CODE1);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE1, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE1, p.toString() + code.getCode());
						break;
					case T2:
						p = tr.getValues().get(Parameters.ERROR_CODE2);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE2, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE2, p.toString() + code.getCode());
						break;
					case U:
						if (device.isControlPower()) {
							p = tr.getValues().get(Parameters.ERROR_CODE1);
							if (p == null)
								tr.getValues().put(Parameters.ERROR_CODE1, code.getCode());
							else
								tr.getValues().put(Parameters.ERROR_CODE1, p.toString() + code.getCode());
						}
						break;
					case V1:
						p = tr.getValues().get(Parameters.ERROR_CODE1);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE1, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE1, p.toString() + code.getCode());
						break;
					case V2:
						p = tr.getValues().get(Parameters.ERROR_CODE2);
						if (p == null)
							tr.getValues().put(Parameters.ERROR_CODE2, code.getCode());
						else
							tr.getValues().put(Parameters.ERROR_CODE2, p.toString() + code.getCode());
						break;
					}
				}

			}
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