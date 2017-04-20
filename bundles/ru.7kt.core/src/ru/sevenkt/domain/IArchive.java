package ru.sevenkt.domain;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;

public interface IArchive {
	static String dateTimeFormatt="yyyy-MM-dd_HH-mm";
	
	ISettings getSettings();

	MonthArchive getMonthArchive();

	ICurrentData getCurrentData();

	HourArchive getHourArchive();

	DayArchive getDayArchive();

	IJournalSettings getJournalSettings();
	
	byte[] getData();
	
	default void toFile(String path) throws IOException{
		byte[] data = getData();
		byte[] fileData = new byte[data.length+64];
		for(int i=0; i<data.length; i++){
			fileData[i+64]=data[i];
		}
		FileUtils.writeByteArrayToFile(new File(path+"\\"+getName()+".bin"), fileData);
	}

	default String getName(){
		LocalDateTime currentDateTime = getCurrentData().getCurrentDateTime();
		String name="0"+getSettings().getSerialNumber()+"_"+DateTimeFormatter.ofPattern(dateTimeFormatt).format(currentDateTime);
		return name;
	}
	
}
