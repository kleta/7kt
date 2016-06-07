package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Embeddable;

import lombok.Data;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Embeddable
@Data
public class MeasuringPK implements Serializable{
	
	private static final long serialVersionUID = 3757951555915656757L;

	private LocalDateTime dateTime;
	
	private Parameters parameter;

	private ArchiveTypes archiveType;

	private Integer idDevice;	
	
}
