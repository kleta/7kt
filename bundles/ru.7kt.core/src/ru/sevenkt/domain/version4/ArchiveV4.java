package ru.sevenkt.domain.version4;

import java.io.File;
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
import ru.sevenkt.exceptions.VersionNotSupportedException;

@Length(58060)
public class ArchiveV4 implements IArchive{
	private byte[] data;

	@Address(value = 0)
	private SettingsV4 settings;

	@Address(value = 80)
	private CurrentDataV4 currentData;

	@Address(value = 300)
	private MonthArchiveV4 monthArchive;

	@Address(value = 3000)
	private DayArchiveV4 dayArchive;

	@Address(value = 14000)
	private HourArchiveV4 hourArchive;

	@Address(value = 55500)
	private JournalSettingsV4 journalSettings;

	public ArchiveV4(byte[] data) throws Exception {
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

	public void setSettings(SettingsV4 settings) {
		this.settings = settings;
	}

	public ICurrentData getCurrentData() {
		return currentData;
	}

	public void setCurrentData(CurrentDataV4 currentData) {
		this.currentData = currentData;
	}

	public MonthArchiveV4 getMonthArchive() {
		return monthArchive;
	}

	public void setMonthArchive(MonthArchiveV4 monthArchive) {
		this.monthArchive = monthArchive;
	}

	public DayArchive getDayArchive() {
		return dayArchive;
	}

	public void setDayArchive(DayArchiveV4 dayArchive) {
		this.dayArchive = dayArchive;
	}

	public HourArchive getHourArchive() {
		return hourArchive;
	}

	public void setHourArchive(HourArchiveV4 hourArchive) {
		this.hourArchive = hourArchive;
	}

	public IJournalSettings getJournalSettings() {
		return journalSettings;
	}

	public void setJournalSettings(JournalSettingsV4 journalSettings) {
		this.journalSettings = journalSettings;
	}

	@Override
	public File toFile() {
		// TODO Auto-generated method stub
		return null;
	}
}
