package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ru.sevenkt.domain.ArchiveTypes;

@Entity
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	private ArchiveTypes type;

	private String templateName;

	@ManyToOne
	@JoinColumn(name = "idDevice")
	private Device device;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArchiveTypes getType() {
		return type;
	}

	public void setType(ArchiveTypes type) {
		this.type = type;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
