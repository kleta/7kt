package ru.sevenkt.app.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import ru.sevenkt.domain.Parameters;

public class DateTimeTableRow implements TableRow{
	
	private LocalDateTime firstColumn;
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	
	private Map<Parameters, Object> values;

	public String getFirstColumn() {
		if(firstColumn==null)
			return null;
		return firstColumn.format(formatter);
	}

	public void setFirstColumn(String dateTime) {
		this.firstColumn = LocalDateTime.parse(dateTime, formatter);
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
