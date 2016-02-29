package ru.sevenkt.app.ui;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import ru.sevenkt.domain.Parameters;

public class ArchiveColumnLabelProvider extends ColumnLabelProvider {
	private Parameters parameter;
	
	
	public ArchiveColumnLabelProvider(Parameters parameter) {
		super();
		this.parameter = parameter;
	}

	public Image getImage(Object element) {
		
		return null;
	}

	public String getText(Object element) {
		TableRow tr = (TableRow)element;
		Object val = tr.getValues().get(parameter);
		return val == null ? "Нет данных" : val.toString();
	}
}
