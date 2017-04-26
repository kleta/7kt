package ru.sevenkt.app.ui;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class ArchiveColumnLabelProvider extends ColumnLabelProvider {

	private Parameters parameter;
	private ArchiveTypes at;
	private Device device;

	public ArchiveColumnLabelProvider(Parameters parameter, ArchiveTypes at, Device dev) {
		super();
		this.parameter = parameter;
		this.at = at;
		device = dev;
	}

	public Image getImage(Object element) {

		return null;
	}

	public String getText(Object element) {
		TableRow tr = (TableRow) element;
		Object val = tr.getValues().get(parameter);
		if (val == null)
			return "--";
		if (val instanceof String)
			return val.toString();
		if (parameter.equals(Parameters.ERROR_TIME1) || parameter.equals(Parameters.ERROR_TIME2)
				|| parameter.equals(Parameters.NO_ERROR_TIME1) || parameter.equals(Parameters.NO_ERROR_TIME2)) {
			if (val != null && !val.equals("")) {
				BigDecimal bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
			} else
				return "";
		}
		BigDecimal bdVal;
		String category = parameter.getCategory();
		switch (category) {
		case ParametersConst.ENERGY:
			bdVal = new BigDecimal(val.toString());
			return bdVal.setScale(device.getDidgitE(), BigDecimal.ROUND_HALF_UP).toString();
		case ParametersConst.VOLUME:
			bdVal = new BigDecimal(val.toString());
			return bdVal.setScale(device.getDidgitV(), BigDecimal.ROUND_HALF_UP).toString();
		case ParametersConst.WEIGHT:
			bdVal = new BigDecimal(val.toString());
			return bdVal.setScale(device.getDidgitM(), BigDecimal.ROUND_HALF_UP).toString();
		case ParametersConst.PRESSURE:
		case ParametersConst.TEMP:
			if (!(val instanceof String)) {
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
			break;
		case ParametersConst.TIME:
			bdVal = new BigDecimal(val.toString());
			return bdVal.setScale(0, BigDecimal.ROUND_HALF_UP).toString();
		case ParametersConst.CALCULATED:
			switch (parameter) {
			case V1_SUB_V2:
			case V3_SUB_V4:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(device.getDidgitV(), BigDecimal.ROUND_HALF_UP).toString();
			case T1_SUB_T2:
			case T3_SUB_T4:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			case M1_SUB_M2:
			case M3_SUB_M4:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(device.getDidgitM(), BigDecimal.ROUND_HALF_UP).toString();
			default:
				break;
			}
		default:
			break;
		}
		return val.toString();
	}
}
