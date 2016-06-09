package ru.sevenkt.app.ui;

import java.util.Map;

import ru.sevenkt.domain.Parameters;

public interface TableRow {
	
	String getFirstColumn();

	void setFirstColumn(String text);

	Map<Parameters, Object> getValues();

	void setValues(Map<Parameters, Object> values);
}
