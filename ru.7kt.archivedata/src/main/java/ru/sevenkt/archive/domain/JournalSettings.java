package ru.sevenkt.archive.domain;

import java.util.List;

@Length(value = 2560)
public class JournalSettings {
	private byte[] data;
	
	List<JournalSettingsRecord> records;

	public JournalSettings(byte[] data) {
		super();
		this.data = data;
	}
	
	
}
