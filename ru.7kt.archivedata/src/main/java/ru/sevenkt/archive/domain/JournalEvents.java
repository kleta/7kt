package ru.sevenkt.archive.domain;

public enum JournalEvents {
	TIME(1, "Настройка времени"),
	FORMULA(2, "Изменение формулы"),
	TC(4, "Температура"),
	TYPE_TERMO(8,"Тип термодатчиков"),
	L(16,"Л"),
	RESET(32,"Сброс показаний"),
	SET_TP(64,"Калибровка датчиков");
	
	
	private int mask;
	private String name;
	JournalEvents(int mask, String name){
		this.mask=mask;
		this.name=name;
	}
	public int getMask() {
		return mask;
	}
	public String getName() {
		return name;
	}
	
}
