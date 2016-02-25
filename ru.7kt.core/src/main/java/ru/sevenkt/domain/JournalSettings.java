package ru.sevenkt.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.sevenkt.annotations.Length;

@Length(value = 2560)
@Data
public class JournalSettings {
	//private byte[] data;
	
	List<JournalSettingsRecord> records;

	public JournalSettings(byte[] data) throws Exception {
		records=new ArrayList<JournalSettingsRecord>();
		if(data[0]==(byte)0xFA){
			int recordLength = JournalSettingsRecord.class.getAnnotation(Length.class).value();
			int address = 0;
			
			for(int i=0; i<=data[2]; i++){
				byte[] rd = new byte[recordLength];		
				for(int j=0; j<recordLength; j++){
					rd[j]=data[3+address+j];
				}
				JournalSettingsRecord record = new JournalSettingsRecord(rd);
				records.add(record);
				address+=recordLength;
			}
		}
		
			
	}
	
}
