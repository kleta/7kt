package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;



public enum Parameter {
	W1(1,"Объем V1, м3"),
	W2(2,"Объем V2, м3"),
	W3(3,"Объем V2, м3"),
	W4(4,"Объем V2, м3"),
	E1(5,"Энергия Е1, Гкал"),
	E2(6,"Энергия Е2, Гкал"),
	M1(7, "Масса М1, т"),
	M2(8, "Масса М2, т"),
	M3(9, "Масса М3, т"),
	M4(10, "Масса М4, т"),
	WORK(11, "Наработка, ч");
	
	Parameter(int id, String name){
		this.setId(id);
		this.setName(name);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int id;

	private String name;

}
