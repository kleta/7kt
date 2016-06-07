package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.domain.ArchiveTypes;

@Converter(autoApply = true)
public class ArchiveTypeConverter implements AttributeConverter<ArchiveTypes, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ArchiveTypes attribute) {
		if(attribute==null)
			System.out.println();
		return attribute.getId();
	}

	@Override
	public ArchiveTypes convertToEntityAttribute(Integer dbData) {
		for (ArchiveTypes at : ArchiveTypes.values()) {
			if(at.getId()==dbData)
				return at;
		}
		return null;
	}
}
