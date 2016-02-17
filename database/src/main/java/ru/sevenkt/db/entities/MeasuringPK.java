package ru.sevenkt.db.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

@Embeddable
@Data
public class MeasuringPK {
	
	private Parameters parametr;

	private ArchiveTypes archiveType;

	private LocalDateTime dateTime;
	
	@Column(name="IDDEVICE", insertable=false, updatable=false)
	private Long idDevice;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MeasuringPK other = (MeasuringPK) obj;
		if (archiveType != other.archiveType)
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idDevice == null) {
			if (other.idDevice != null)
				return false;
		} else if (!idDevice.equals(other.idDevice))
			return false;
		if (parametr != other.parametr)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((archiveType == null) ? 0 : archiveType.hashCode());
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((idDevice == null) ? 0 : idDevice.hashCode());
		result = prime * result + ((parametr == null) ? 0 : parametr.hashCode());
		return result;
	}
	
	
}
