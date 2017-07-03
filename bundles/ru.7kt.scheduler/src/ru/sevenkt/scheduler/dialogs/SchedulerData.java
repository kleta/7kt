package ru.sevenkt.scheduler.dialogs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Locale;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.CronFieldName;
import com.cronutils.parser.CronParser;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.domain.ArchiveTypes;

public class SchedulerData {
	private String name;

	private int dayShift;
	
	private boolean monthCheck;

	private boolean dayCheck;

	private boolean hourCheck;

	
	private String minutes;
	
	private String hours;
	
	private String dayOfMonth;
	
	private String month;
	
	private String dayOfWeek;

	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	private List<Device> devices;

	public SchedulerData(String minutes, String hours, String dayOfMonth, String month, String dayOfWeek) {
		super();
		this.minutes = minutes;
		this.hours = hours;
		this.dayOfMonth = dayOfMonth;
		this.month = month;
		this.dayOfWeek = dayOfWeek;
	}

	public SchedulerData(SchedulerGroup gr) {
		CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		CronParser parser = new CronParser(cronDefinition);
		Cron cron = parser.parse(gr.getCronString());
		minutes=cron.retrieve(CronFieldName.MINUTE).getExpression().asString();
		hours=cron.retrieve(CronFieldName.HOUR).getExpression().asString();
		dayOfMonth=cron.retrieve(CronFieldName.DAY_OF_MONTH).getExpression().asString();
		month=cron.retrieve(CronFieldName.MONTH).getExpression().asString();
		dayOfWeek=cron.retrieve(CronFieldName.DAY_OF_WEEK).getExpression().asString();
		List<ArchiveType> ats = gr.getArchiveTypes();
		for (ArchiveType archiveType : ats) {
			ArchiveTypes at = archiveType.getId();
			if(at.equals(ArchiveTypes.MONTH))
				monthCheck=true;
			if(at.equals(ArchiveTypes.DAY))
				dayCheck=true;
			if(at.equals(ArchiveTypes.HOUR))
				hourCheck=true;
		}
		name=gr.getName();
		dayShift=gr.getDeepDay();
		devices=gr.getDevices();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		firePropertyChange("minute", this.minutes, this.minutes = minutes);
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		firePropertyChange("hours", this.hours, this.hours = hours);
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		firePropertyChange("dayOfMonth", this.dayOfMonth, this.dayOfMonth = dayOfMonth);
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		firePropertyChange("month", this.month, this.month = month);
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		firePropertyChange("dayOfWeek", this.dayOfWeek, this.dayOfWeek = dayOfWeek);
	}

	public String generateCronExpression() {
		return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", "0", minutes, hours, dayOfMonth, month, dayOfWeek,
				"*");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public int getDayShift() {
		return dayShift;
	}

	public void setDayShift(int dayShift) {
		firePropertyChange("dayShift", this.dayShift, this.dayShift = dayShift);
	}

	public boolean isMonthCheck() {
		return monthCheck;
	}

	public void setMonthCheck(boolean monthCheck) {
		firePropertyChange("monthCheck", this.monthCheck, this.monthCheck = monthCheck);
	}

	public boolean isDayCheck() {
		return dayCheck;
	}

	public void setDayCheck(boolean dayCheck) {
		firePropertyChange("dayCheck", this.dayCheck, this.dayCheck= dayCheck);
	}

	public boolean isHourCheck() {
		return hourCheck;
	}

	public void setHourCheck(boolean hourCheck) {
		firePropertyChange("hourCheck", this.hourCheck, this.hourCheck= hourCheck);
	}

	public void setSelectedDevice(List<Device> selected) {
		devices=selected;	
	}
	
	public List<Device> getSelectedDevice(){
		return devices;
	}
	
}