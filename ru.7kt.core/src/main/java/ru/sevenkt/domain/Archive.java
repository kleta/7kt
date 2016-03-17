package ru.sevenkt.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.exceptions.VersionNotSupportedException;

@Length(58060)
public class Archive {
	private byte[] data;

	@Address(value = 0)
	private Settings settings;

	@Address(value = 80)
	private CurrentData currentData;

	@Address(value = 300)
	private MonthArchive monthArchive;

	@Address(value = 3000)
	private DayArchive dayArchive;

	@Address(value = 13500)
	private HourArchive hourArchive;

	@Address(value = 55500)
	private JournalSettings journalSettings;

	public Archive(byte[] data) throws Exception {
		this.data = data;
		init();
	}

	private void init() throws Exception {
		Field[] fields = getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Address.class)) {
				Class<?> fieldType = field.getType();
				int dataSize = fieldType.getAnnotation(Length.class).value();
				byte[] fieldData = new byte[dataSize];
				for (int i = 0; i < dataSize; i++) {
					fieldData[i] = data[(int) (field.getAnnotation(Address.class).value() + i)];
				}
				Constructor<?> cons = fieldType.getConstructor(byte[].class);
				Object obj = cons.newInstance(fieldData);
				field.set(this, obj);
				if(field.getName().equals("settings")){
					if(!settings.isVersionSupport())
							throw new VersionNotSupportedException("Версия архива "+settings.getArchiveVersion()+" не поддерживается");
				}
			}
		}
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public CurrentData getCurrentData() {
		return currentData;
	}

	public void setCurrentData(CurrentData currentData) {
		this.currentData = currentData;
	}

	public MonthArchive getMonthArchive() {
		return monthArchive;
	}

	public void setMonthArchive(MonthArchive monthArchive) {
		this.monthArchive = monthArchive;
	}

	public DayArchive getDayArchive() {
		return dayArchive;
	}

	public void setDayArchive(DayArchive dayArchive) {
		this.dayArchive = dayArchive;
	}

	public HourArchive getHourArchive() {
		return hourArchive;
	}

	public void setHourArchive(HourArchive hourArchive) {
		this.hourArchive = hourArchive;
	}

	public JournalSettings getJournalSettings() {
		return journalSettings;
	}

	public void setJournalSettings(JournalSettings journalSettings) {
		this.journalSettings = journalSettings;
	}
}
