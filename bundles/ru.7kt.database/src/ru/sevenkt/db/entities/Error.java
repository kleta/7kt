package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.ErrorCodes;

@Entity
@Table(name="Errors")
public class Error {
	
	@EmbeddedId
	private ErrorPK id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "idDevice", referencedColumnName = "id", insertable = false, updatable = false)
	private Device device;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime dateTime;

	@Column(insertable = false, updatable = false)
	private ErrorCodes errorCode;

	@Column(insertable = false, updatable = false)
	private ArchiveTypes archiveType;

	private LocalDateTime timestamp;
	
	public void setArchiveType(ArchiveTypes month) {
		if (id == null)
			id = new ErrorPK();
		archiveType=month;
		id.setArchiveType(month);
	}

	public void setDateTime(LocalDateTime atTime) {
		if (id == null)
			id = new ErrorPK();
		dateTime=atTime;
		id.setDateTime(atTime);
	}

	

	public void setDevice(Device device) {
		if (id == null)
			id = new ErrorPK();
		this.device = device;
		id.setIdDevice(device.getId());
	}
	
	public void setErrorCode(ErrorCodes code) {
		if (id == null)
			id = new ErrorPK();
		this.errorCode = code;
		id.setErrorCode(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Error other = (Error) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public ErrorPK getId() {
		return id;
	}

	public void setId(ErrorPK id) {
		this.id = id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Device getDevice() {
		return device;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public ErrorCodes getErrorCode() {
		return errorCode;
	}

	public ArchiveTypes getArchiveType() {
		return archiveType;
	}
}
