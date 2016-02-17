package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.domain.Parameters;

@Converter(autoApply = true)
public class ParameterConverter implements AttributeConverter<Parameters, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Parameters attribute) {
		return attribute.getId();
	}

	@Override
	public Parameters convertToEntityAttribute(Integer dbData) {
		for (Parameters p : Parameters.values()) {
			if (p.getId() == dbData)
				return p;
		}
		return null;
	}

}
