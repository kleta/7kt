package ru.sevenkt.app.ui;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import ru.sevenkt.domain.Parameters;

public class TableRow {
	
	private LocalDateTime dateTime;
	
	private Map<Parameters, Object> values;

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
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
