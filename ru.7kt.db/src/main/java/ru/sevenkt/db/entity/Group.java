package ru.sevenkt.db.entity;

import lombok.Data;

@Entity
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@ManyToMany(mappedBy="groups")
	private List<Device> devices;
	
}
