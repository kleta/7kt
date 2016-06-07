package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Data;
import ru.sevenkt.domain.JournalEvents;

@Embeddable
@Data
public class JournalPK implements Serializable{
	
	private static final long serialVersionUID = 3757951555915656757L;

	private LocalDateTime dateTime;
	
	private Integer idDevice;
	
	private JournalEvents event;
	
}
