package ru.sevenkt.db.entity;

@Entity
public class Parameter {
	
	@Id
	private Long id;
	
	private String name;
	
	private String unit;
}
