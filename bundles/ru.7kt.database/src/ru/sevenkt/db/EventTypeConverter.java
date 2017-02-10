package ru.sevenkt.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ru.sevenkt.domain.JournalEvents;

@Converter(autoApply = true)
public class EventTypeConverter implements AttributeConverter<JournalEvents, Integer> {

	

	@Override
	public Integer convertToDatabaseColumn(JournalEvents attribute) {
		return attribute.getMask();
	}

	@Override
	public JournalEvents convertToEntityAttribute(Integer dbData) {
		JournalEvents[] events = JournalEvents.values();
		for (JournalEvents journalEvents : events) {
			if(journalEvents.getMask()==dbData)
				return journalEvents;
		}
		return null;
	}
}
