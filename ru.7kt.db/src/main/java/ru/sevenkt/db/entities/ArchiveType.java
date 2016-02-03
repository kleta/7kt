package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ArchiveType {
	@Id
	private Long id;
	
	private String name;
}
