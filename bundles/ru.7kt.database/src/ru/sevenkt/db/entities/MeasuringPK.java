package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Embeddable
public class MeasuringPK implements Serializable{
	
	private static final long serialVersionUID = 3757951555915656757L;

	private LocalDateTime dateTime;
	
	private Parameters parameter;

	private ArchiveTypes archiveType;

	private Integer idDevice;	
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((archiveType == null) ? 0 : archiveType.hashCode());
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((idDevice == null) ? 0 : idDevice.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasuringPK other = (MeasuringPK) obj;
		if (archiveType != other.archiveType)
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idDevice == null) {
			if (other.idDevice != null)
				return false;
		} else if (!idDevice.equals(other.idDevice))
			return false;
		if (parameter != other.parameter)
			return false;
		return true;
	}

	
	
}
