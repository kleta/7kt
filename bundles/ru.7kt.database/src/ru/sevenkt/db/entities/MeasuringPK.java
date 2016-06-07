package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Embeddable
public class MeasuringPK implements Serializable{
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Parameters getParameter() {
		return parameter;
	}

	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}

	public ArchiveTypes getArchiveType() {
		return archiveType;
	}

	public void setArchiveType(ArchiveTypes archiveType) {
		this.archiveType = archiveType;
	}

	public Integer getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(Integer idDevice) {
		this.idDevice = idDevice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 3757951555915656757L;

	private LocalDateTime dateTime;
	
	private Parameters parameter;

	private ArchiveTypes archiveType;

	private Integer idDevice;	
	
}
