package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.ErrorCodes;

@Embeddable
public class ErrorPK implements Serializable {
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes errorCode) {
		this.errorCode = errorCode;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 3075370590240558439L;

	private LocalDateTime dateTime;

	private ErrorCodes errorCode;

	private ArchiveTypes archiveType;

	private Integer idDevice;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorPK other = (ErrorPK) obj;
		if (archiveType != other.archiveType)
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (errorCode != other.errorCode)
			return false;
		if (idDevice == null) {
			if (other.idDevice != null)
				return false;
		} else if (!idDevice.equals(other.idDevice))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((archiveType == null) ? 0 : archiveType.hashCode());
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((idDevice == null) ? 0 : idDevice.hashCode());
		return result;
	}
	
	

}
