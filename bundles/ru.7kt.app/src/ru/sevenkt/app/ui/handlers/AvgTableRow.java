package ru.sevenkt.app.ui.handlers;

import java.util.HashMap;
import java.util.Map;

import ru.sevenkt.app.ui.TableRow;
import ru.sevenkt.domain.Parameters;

public class AvgTableRow implements TableRow {

	private Map<Parameters, Object> values;

	private String str;

	@Override
	public String getFirstColumn() {
		return str;
	}

	@Override
	public void setFirstColumn(String text) {
		str = text;

	}

	public Map<Parameters, Object> getValues() {
		if (values == null)
			values = new HashMap<>();
		return values;
	}

	public void setValues(Map<Parameters, Object> values) {
		this.values = values;
	}

}
