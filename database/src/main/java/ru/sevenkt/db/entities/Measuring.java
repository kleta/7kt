package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "idDevice", referencedColumnName = "id", insertable = false, updatable = false)
	private Device device;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime dateTime;

	@Column(insertable = false, updatable = false)
	private Parameters parameter;

	@Column(insertable = false, updatable = false)
	private ArchiveTypes archiveType;

	private Float value;

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

	public void setParametr(Parameters value2) {
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
