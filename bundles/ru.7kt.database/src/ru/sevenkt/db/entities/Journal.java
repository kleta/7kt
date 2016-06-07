package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ru.sevenkt.domain.JournalEvents;

@Entity
@Table(name="JournalSettings")
public class Journal {

	public JournalPK getId() {
		return id;
	}

	public void setId(JournalPK id) {
		this.id = id;
	}

	public long getWorkHour() {
		return workHour;
	}

	public void setWorkHour(long workHour) {
		this.workHour = workHour;
	}

	public Device getDevice() {
		return device;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public JournalEvents getEvent() {
		return event;
	}

	@EmbeddedId
	private JournalPK id;

	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name = "idDevice", referencedColumnName = "id", insertable = false, updatable = false)
	private Device device;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime dateTime;
	
	@Column(insertable = false, updatable = false)
	private JournalEvents event;
	
	private long workHour;

	

	

	public void setDateTime(LocalDateTime atTime) {
		if (id == null)
			id = new JournalPK();
		dateTime=atTime;
		id.setDateTime(atTime);
	}

	public void setEvent(JournalEvents type){
		if (id == null)
			id = new JournalPK();
		event=type;
		id.setEvent(type);
	}

	public void setDevice(Device device) {
		if (id == null)
			id = new JournalPK();
		this.device = device;
		id.setIdDevice(device.getId());
	}

}
