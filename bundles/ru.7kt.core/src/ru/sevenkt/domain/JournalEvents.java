package ru.sevenkt.domain;

public enum JournalEvents {
	TIME(1,"Изменение времени"),
	FORMULA(2, "Изменение расчетной формулы"),
	TC(4, "Изменение температуры ХВ"),
	TYPE_TERMO(8,"Изменение типа термодатчика"),
	L(16, "Изменение установок расхода"),
	RESET(32,"Обнуление показаний"),
	SET_TP(64,"Изменение настроек T,P");
	
	
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
