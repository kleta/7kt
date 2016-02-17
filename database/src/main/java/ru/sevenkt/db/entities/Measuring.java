package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Entity
@Data
public class Measuring {
	
	@EmbeddedId
	private MeasuringPK id;
	
	@ManyToOne
	@JoinColumn(name="idDevice")
	private Device device;
	
	
	private Float value;

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Parameters getParametr() {
		return id.getParametr();
	}

	public void setParametr(Parameters parametr) {
		 id.setParametr(parametr);
	}

	public ArchiveTypes getArchiveType() {
		return id.getArchiveType();
	}

	public void setArchiveType(ArchiveTypes archiveType) {
		id.setArchiveType(archiveType);
	}

	public LocalDateTime getDateTime() {
		return id.getDateTime();
	}

	public void setDateTime(LocalDateTime dateTime) {
		id.setDateTime(dateTime);
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
}
