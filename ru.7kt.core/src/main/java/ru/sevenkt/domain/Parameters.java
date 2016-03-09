package ru.sevenkt.domain;

public enum Parameters {
	V1(1,"V1", ParametersConst.VOLUME, "м3"),
	V2(2,"V2", ParametersConst.VOLUME, "м3"),
	V3(3,"V3", ParametersConst.VOLUME, "м3"),
	V4(4,"V4", ParametersConst.VOLUME, "м3"),
	E1(5,"Е1", ParametersConst.ENERGY, "Гкал"),
	E2(6,"Е2", ParametersConst.ENERGY, "Гкал"),
	M1(7, "М1", ParametersConst.WEIGHT, "т"),
	M2(8, "М2", ParametersConst.WEIGHT, "т"),
	M3(9, "М3", ParametersConst.WEIGHT, "т"),
	M4(10, "М4", ParametersConst.WEIGHT, "т"),
	WORK(11, "Наработка", ParametersConst.TIME, "ч"), 
	AVG_TEMP1(12, "T1", ParametersConst.TEMP, "°C"),
	AVG_TEMP2(13, "T2", ParametersConst.TEMP, "°C"),
	AVG_TEMP3(14, "T3", ParametersConst.TEMP, "°C"),
	AVG_TEMP4(15, "T4", ParametersConst.TEMP, "°C"),
	AVG_P1(16, "P1", ParametersConst.PRESSURE, ""),
	AVG_P2(17, "P2", ParametersConst.PRESSURE, "");
	

	Parameters(int id, String name, String category, String unit){
		this.id=id;
		this.name=name;
		this.category=category;
		this.unit=unit;
	}
	
	

	

	private int id;

	private String name;
	
	private String category;
	
	private String unit;

	public String getUnit() {
		return unit;
	}

	public String getCategory() {
		return category;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	

}