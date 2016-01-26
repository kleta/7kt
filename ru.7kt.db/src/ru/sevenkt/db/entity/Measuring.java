package ru.sevenkt.db.entity;

import java.time.LocalDateTime;

public class Measuring {
	
	private Device device;
	
	private Parameter parametr;
	
	private ArchiveType archiveType;
	
	private LocalDateTime dateTime;
	
	private Float value;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Parameter getParametr() {
		return parametr;
	}

	public void setParametr(Parameter parametr) {
		this.parametr = parametr;
	}

	public ArchiveType getArchiveType() {
		return archiveType;
	}

	public void setArchiveType(ArchiveType archiveType) {
		this.archiveType = archiveType;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
