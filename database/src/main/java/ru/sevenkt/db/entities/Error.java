package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.ErrorCodes;

@Entity
@Table(name="Errors")
@Data
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
}
