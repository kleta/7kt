package ru.sevenkt.app.ui;

import java.math.BigDecimal;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;
import ru.sevenkt.domain.ParametersConst;

public class ArchiveColumnLabelProvider extends ColumnLabelProvider {

	private Parameters parameter;
	private ArchiveTypes at;

	public ArchiveColumnLabelProvider(Parameters parameter, ArchiveTypes at) {
		super();
		this.parameter = parameter;
		this.at = at;
	}

	public Image getImage(Object element) {

		return null;
	}

	public String getText(Object element) {
		TableRow tr = (TableRow) element;
		Object val = tr.getValues().get(parameter);
		if (parameter.equals(Parameters.ERROR_TIME1) || parameter.equals(Parameters.ERROR_TIME2)
				|| parameter.equals(Parameters.NO_ERROR_TIME1) || parameter.equals(Parameters.NO_ERROR_TIME2)) {
			if (val != null && !val.equals("")) {
				long round = Math.round((double) val);
				return round + "";
			} else
				return "";
		}
//		if(parameter.getCategory().equals(ParametersConst.TEMP)){
//			if((double)val<-60 || (double)val>150)
//				return "err";
//		}
		if (val instanceof Double) {
			BigDecimal bdVal;
			switch (at) {
			case MONTH:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			case DAY:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			case HOUR:
				bdVal = new BigDecimal(val.toString());
				return bdVal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
		}
		if (val instanceof String)
			return val.toString();
		if (parameter.equals(Parameters.ERROR_CODE1) || parameter.equals(Parameters.ERROR_CODE2))
			return val == null ? "" : val.toString();
		return val == null ? "Нет данных" : val.toString();
	}
}
