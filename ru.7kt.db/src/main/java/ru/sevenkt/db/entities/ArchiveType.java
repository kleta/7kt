package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ArchiveType {
	@Id
	private Long id;
	
	private String name;
}
