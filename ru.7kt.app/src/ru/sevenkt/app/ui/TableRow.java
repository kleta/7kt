package ru.sevenkt.app.ui;

import java.util.HashMap;
import java.util.Map;

import ru.sevenkt.domain.Parameters;

public class TableRow {
	
	private Object firstColumn;
	
	private Map<Parameters, Object> values;

	public Object getDateTime() {
		return firstColumn;
	}

	public void setDateTime(Object dateTime) {
		this.firstColumn = dateTime;
	}

	public Map<Parameters, Object> getValues() {
		if(values==null)
			values=new HashMap<>();
		return values;
	}

	public void setValues(Map<Parameters, Object> values) {
		this.values = values;
	}
	
}
