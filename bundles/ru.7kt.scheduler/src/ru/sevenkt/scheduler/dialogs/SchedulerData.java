package ru.sevenkt.scheduler.dialogs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import ru.sevenkt.db.entities.Device;

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