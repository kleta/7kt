package ru.sevenkt.domain.version1;

import java.util.ArrayList;
import java.util.List;

import ru.sevenkt.annotations.Length;
import ru.sevenkt.domain.IJournalSettings;
import ru.sevenkt.domain.IJournalSettingsRecord;

@Length(value = 2560)
public class JournalSettingsV1 implements IJournalSettings{
	//private byte[] data;
	
	List<IJournalSettingsRecord> records;

	public JournalSettingsV1(byte[] data) throws Exception {
		records=new ArrayList<IJournalSettingsRecord>();
		if(data[0]==(byte)0xFA){
			int recordLength = JournalSettingsRecordV1.class.getAnnotation(Length.class).value();
			int address = 0;
			
			for(int i=0; i<=data[2]; i++){
				byte[] rd = new byte[recordLength];		
				for(int j=0; j<recordLength; j++){
					rd[j]=data[3+address+j];
				}
				JournalSettingsRecordV1 record = new JournalSettingsRecordV1(rd);
				records.add(record);
				address+=recordLength;
			}
		}
		
			
	}

	public List<IJournalSettingsRecord> getRecords() {
		return records;
	}

	public void setRecords(List<IJournalSettingsRecord> records) {
		this.records = records;
	}
	
}
