package ru.sevenkt.db;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {
		Instant instant =attribute.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		Instant instant = Instant.ofEpochMilli(dbData.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
