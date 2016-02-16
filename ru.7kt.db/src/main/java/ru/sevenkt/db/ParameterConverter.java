package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.db.entities.Parameter;

@Converter(autoApply = true)
public class ParameterConverter implements AttributeConverter<Parameter, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Parameter attribute) {
		return attribute.getId();
	}

	@Override
	public Parameter convertToEntityAttribute(Integer dbData) {
		for (Parameter p : Parameter.values()) {
			if (p.getId() == dbData)
				return p;
		}
		return null;
	}

}
