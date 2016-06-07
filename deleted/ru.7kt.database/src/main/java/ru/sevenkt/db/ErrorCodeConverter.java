package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.domain.ErrorCodes;

@Converter(autoApply = true)
public class ErrorCodeConverter implements AttributeConverter<ErrorCodes, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ErrorCodes attribute) {
		if (attribute != null)
			return attribute.getId();
		return null;
	}

	@Override
	public ErrorCodes convertToEntityAttribute(Integer dbData) {
		if (dbData != null)
			for (ErrorCodes p : ErrorCodes.values()) {
				if (p.getId() == dbData)
					return p;
			}
		return null;
	}

}
