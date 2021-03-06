package ru.sevenkt.domain;

public enum Parameters {
	V1(1,"V1", ParametersConst.VOLUME, "м3", 9),
	V2(2,"V2", ParametersConst.VOLUME, "м3", 10),
	V3(3,"V3", ParametersConst.VOLUME, "м3", 12),
	V4(4,"V4", ParametersConst.VOLUME, "м3", 13),
	E1(5,"Е1", ParametersConst.ENERGY, "Гкал", 1),
	E2(6,"Е2", ParametersConst.ENERGY, "Гкал", 2),
	M1(7, "М1", ParametersConst.WEIGHT, "т", 15),
	M2(8, "М2", ParametersConst.WEIGHT, "т", 16),
	M3(9, "М3", ParametersConst.WEIGHT, "т", 18),
	M4(10, "М4", ParametersConst.WEIGHT, "т", 19),
	NO_ERROR_TIME1(11, "ВНР1", ParametersConst.TIME, "час", 29), 
	AVG_TEMP1(12, "T1", ParametersConst.TEMP, "°C", 3),
	AVG_TEMP2(13, "T2", ParametersConst.TEMP, "°C", 4),
	AVG_TEMP3(14, "T3", ParametersConst.TEMP, "°C", 6),
	AVG_TEMP4(15, "T4", ParametersConst.TEMP, "°C", 7),
	AVG_P1(16, "P1", ParametersConst.PRESSURE, "кг/см2", 21),
	AVG_P2(17, "P2", ParametersConst.PRESSURE, "кг/см2", 22),
	V1_SUB_V2(18, "V1-V2", ParametersConst.CALCULATED, "м3", 11),
	V3_SUB_V4(19, "V3-V4", ParametersConst.CALCULATED, "м3", 14),
	M1_SUB_M2(20, "M1-M2", ParametersConst.CALCULATED, "т", 17),
	M3_SUB_M4(21, "M3-M4", ParametersConst.CALCULATED, "т", 20),
	T1_SUB_T2(22, "T1-T2", ParametersConst.CALCULATED, "°C", 5),
	T3_SUB_T4(23, "T3-T4", ParametersConst.CALCULATED, "°C", 8),
	ERROR_CODE1(24, "ОШ1",ParametersConst.ERROR,"код", 27),
	V5(25,"V5", ParametersConst.VOLUME, "м3", 33),
	V6(26,"V6", ParametersConst.VOLUME, "м3", 34),
	V7(27,"V7", ParametersConst.VOLUME, "м3", 35),
	V8(28,"V8", ParametersConst.VOLUME, "м3", 36),
	ERROR_TIME1(29, "ВОС1",ParametersConst.TIME,"час", 40),
	ERROR_CODE2(30, "ОШ2",ParametersConst.ERROR,"код", 28),
	NO_ERROR_TIME2(31, "ВНР2", ParametersConst.TIME, "час", 41), 
	ERROR_TIME2(32, "ВОС2",ParametersConst.TIME,"час", 42),
	WORK(33,"Время работы",ParametersConst.TIME, "час", 43),
	ERROR_BYTE1(34,"Байт ошибки1",ParametersConst.ERROR, "час", 44),
	ERROR_BYTE2(35,"Байт ошибки2",ParametersConst.ERROR, "час", 45),
	AVG_P3(36, "P1", ParametersConst.PRESSURE, "кг/см2", 23),
	AVG_P4(37, "P2", ParametersConst.PRESSURE, "кг/см2", 24);
	
	

	

	Parameters(int id, String name, String category, String unit, int orderIndex){
		this.id=id;
		this.name=name;
		this.category=category;
		this.unit=unit;
		this.orderIndex=orderIndex;
		
	}
	
	

	

	private int id;

	private String name;
	
	private String category;
	
	private String unit;
	
	private int orderIndex;

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

	public int getOrderIndex() {
		return orderIndex;
	}
	
	

}