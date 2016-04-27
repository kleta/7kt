package ru.sevenkt.domain;

public enum ErrorCodes {
	T1(1, "T"),
	V1(2, "V"),
	E1(3, "E"),
	T2(4, "T"),
	V2(5, "V"),
	E2(6, "E"),
	U(7, "U");

	ErrorCodes(int id, String code) {
		this.id=id;
		this.code=code;
	}

	
	int id;

	String code;

	public Integer getId() {
		return id;
	}
}
