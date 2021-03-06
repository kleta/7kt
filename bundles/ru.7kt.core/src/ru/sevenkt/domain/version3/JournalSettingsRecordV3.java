package ru.sevenkt.domain.version3;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.IJournalSettingsRecord;
import ru.sevenkt.domain.JournalEvents;
import ru.sevenkt.utils.DataUtils;

@Length(10)
public class JournalSettingsRecordV3 implements IJournalSettingsRecord{
	
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
	
	public JournalSettingsRecordV3(byte[] data) throws Exception {
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

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(int monthYear) {
		this.monthYear = monthYear;
	}

	public long getWorkHour() {
		return workHour;
	}

	public void setWorkHour(long workHour) {
		this.workHour = workHour;
	}

	public JournalEvents getEvent() {
		return event;
	}

	public void setEvent(JournalEvents event) {
		this.event = event;
	}

	public LocalDateTime getDateTime() {
		String b = Integer.toBinaryString((monthYear & 0xF0));
		int year = ((monthYear & 0xF0) / 16)  + 2000;
		year = year < 2015 ? year + 16 : year;
		String a = Integer.toBinaryString(year);
		int month = monthYear & 0xF;
		if(month<1 || month>12 || day<1 || day>31)
			return null;
		return LocalDateTime.of(year, month, getDay(), getHour(), 0);
	}

	@Override
	public String toString() {
		return getDateTime()+"-"+"JournalSettingsRecordV3 [hour=" + hour + ", day=" + day + ", monthYear=" + monthYear + ", workHour="
				+ workHour + ", event=" + event + "]";
	}
	
}

