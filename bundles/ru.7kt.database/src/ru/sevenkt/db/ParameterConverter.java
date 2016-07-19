package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.domain.Parameters;

@Converter(autoApply = true)
public class ParameterConverter implements AttributeConverter<Parameters, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Parameters attribute) {
		if (attribute != null)
			return attribute.getId();
		return null;
	}

	@Override
	public Parameters convertToEntityAttribute(Integer dbData) {
		if (dbData != null)
			for (Parameters p : Parameters.values()) {
				if (p.getId() == dbData)
					return p;
			}
		return null;
	}

}
