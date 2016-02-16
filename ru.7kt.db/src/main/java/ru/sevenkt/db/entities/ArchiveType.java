package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;


public enum ArchiveType {
	MONTH(1, "Месячный архив"),
	DAY(2, "Суточный архив"),
	HOUR(3, "Часовой архив");
	
	
	ArchiveType(int id, String name){
		this.id=id;
		this.name=name;
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
