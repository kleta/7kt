package ru.sevenkt.db.entities;

import ru.sevenkt.domain.ArchiveTypes;

public class Report {
	
	private String name;
	
	private ArchiveTypes type;
	
	private String templateName;

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
	
	
	
}
