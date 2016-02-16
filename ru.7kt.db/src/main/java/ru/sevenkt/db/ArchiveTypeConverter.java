package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.db.entities.ArchiveType;

@Converter(autoApply = true)
public class ArchiveTypeConverter implements AttributeConverter<ArchiveType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ArchiveType attribute) {
		return attribute.getId();
	}

	@Override
	public ArchiveType convertToEntityAttribute(Integer dbData) {
		for (ArchiveType at : ArchiveType.values()) {
			if(at.getId()==dbData)
				return at;
		}
		return null;
	}
}
