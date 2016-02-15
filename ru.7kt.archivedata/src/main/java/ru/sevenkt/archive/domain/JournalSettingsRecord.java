package ru.sevenkt.archive.domain;

import java.lang.reflect.Field;
import java.util.Arrays;

import lombok.Data;
import ru.sevenkt.archive.utils.DataUtils;

@Length(10)
@Data
public class JournalSettingsRecord {
	
	@Address(value=0)
	@Length(value=1)
	private int hour;
	
	@Address(value=1)
	@Length(value=1)
	private int day;
	
	@Address(value=2)
	@Length(value=1)
	private int monthYear;

	@Address(value=3)
	@Length(value=4)
	private long workHour;
	
	@Address(value=7)
	@Length(value=1)
	private JournalEvents event;
	
	public JournalSettingsRecord(byte[] data) throws Exception {
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Length.class) && field.isAnnotationPresent(Address.class)) {
				int adr = (int) field.getAnnotation(Address.class).value();
				int len = (int) field.getAnnotation(Length.class).value();
				byte[] bytes = Arrays.copyOfRange(data, adr, adr + len);
				Class<?> type = field.getType();
				if (type.equals(int.class)) {
					int value = DataUtils.getIntValue(bytes);
					field.set(this, value);
				} else if (type.equals(JournalEvents.class)) {
					JournalEvents value = DataUtils.getJournalEvents(bytes);
					field.set(this, value);
				} else if (type.equals(long.class)) {
					long value = DataUtils.getLongValue(bytes);
					field.set(this, value);
				}
			}

		}
	}
}
