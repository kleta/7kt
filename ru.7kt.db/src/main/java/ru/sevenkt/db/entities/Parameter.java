package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Entity
@Data
@Builder
public class Parameter {

	@Id
	private Long id;

	private String name;

	private String unit;

	@Tolerate
	Parameter() {
	}

}
