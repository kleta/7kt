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
import ru.sevenkt.domain.Parameters;

@Entity
@Table(name="Measurings")
public class Measuring {

	public MeasuringPK getId() {
		return id;
	}

	public void setId(MeasuringPK id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
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

	public Parameters getParameter() {
		return parameter;
	}

	public ArchiveTypes getArchiveType() {
		return archiveType;
	}

	@EmbeddedId
	private MeasuringPK id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "idDevice", referencedColumnName = "id", insertable = false, updatable = false)
	private Device device;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime dateTime;

	@Column(insertable = false, updatable = false)
	private Parameters parameter;

	@Column(insertable = false, updatable = false)
	private ArchiveTypes archiveType;

	private Double value;
	
	private LocalDateTime timestamp;

	public void setArchiveType(ArchiveTypes month) {
		if (id == null)
			id = new MeasuringPK();
		archiveType=month;
		id.setArchiveType(month);
	}

	public void setDateTime(LocalDateTime atTime) {
		if (id == null)
			id = new MeasuringPK();
		dateTime=atTime;
		id.setDateTime(atTime);
	}

	public void setParameter(Parameters value2) {
		if (id == null)
			id = new MeasuringPK();
		parameter=value2;
		id.setParameter(value2);
	}

	public void setDevice(Device device) {
		if (id == null)
			id = new MeasuringPK();
		this.device = device;
		id.setIdDevice(device.getId());
	}

}
