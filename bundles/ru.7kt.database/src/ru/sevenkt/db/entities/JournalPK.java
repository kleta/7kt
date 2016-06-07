package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import ru.sevenkt.domain.JournalEvents;

@Embeddable
public class JournalPK implements Serializable{
	
	private static final long serialVersionUID = 3757951555915656757L;

	private LocalDateTime dateTime;
	
	private Integer idDevice;
	
	private JournalEvents event;

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getIdDevice() {
		return idDevice;
	}

	public void setIdDevice(Integer idDevice) {
		this.idDevice = idDevice;
	}

	public JournalEvents getEvent() {
		return event;
	}

	public void setEvent(JournalEvents event) {
		this.event = event;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
