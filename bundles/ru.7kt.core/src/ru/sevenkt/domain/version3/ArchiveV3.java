package ru.sevenkt.domain.version3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import ru.sevenkt.annotations.Address;
import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.IArchive;
import ru.sevenkt.domain.ICurrentData;
import ru.sevenkt.domain.DayArchive;
import ru.sevenkt.domain.HourArchive;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.ISettings;
import ru.sevenkt.domain.MonthArchive;
import ru.sevenkt.exceptions.VersionNotSupportedException;

@Length(58060)
public class ArchiveV3 implements IArchive{
	private byte[] data;

	@Address(value = 0)
	private SettingsV3 settings;

	@Address(value = 80)
	private CurrentDataV3 currentData;

	@Address(value = 300)
	private MonthArchiveV3 monthArchive;

	@Address(value = 3000)
	private DayArchiveV3 dayArchive;

	@Address(value = 13500)
	private HourArchiveV3 hourArchive;

	@Address(value = 55500)
	private JournalSettingsV3 journalSettings;

	public ArchiveV3(byte[] data) throws Exception {
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
				long fromAddress = field.getAnnotation(Address.class).value();
				for (int i = 0; i < dataSize; i++) {			
					fieldData[i] = data[(int) (fromAddress + i)];
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

	public ISettings getSettings() {
		return settings;
	}

	public void setSettings(SettingsV3 settings) {
		this.settings = settings;
	}

	public ICurrentData getCurrentData() {
		return currentData;
	}

	public void setCurrentData(CurrentDataV3 currentData) {
		this.currentData = currentData;
	}

	public MonthArchive getMonthArchive() {
		return monthArchive;
	}

	public void setMonthArchive(MonthArchiveV3 monthArchive) {
		this.monthArchive = monthArchive;
	}

	public DayArchive getDayArchive() {
		return dayArchive;
	}

	public void setDayArchive(DayArchiveV3 dayArchive) {
		this.dayArchive = dayArchive;
	}

	public HourArchive getHourArchive() {
		return hourArchive;
	}

	public void setHourArchive(HourArchiveV3 hourArchive) {
		this.hourArchive = hourArchive;
	}

	public IJournalSettings getJournalSettings() {
		return journalSettings;
	}

	public void setJournalSettings(JournalSettingsV3 journalSettings) {
		this.journalSettings = journalSettings;
	}
}
