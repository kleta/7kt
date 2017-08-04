package ru.sevenkt.db.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="SchedulerGroup")
public class SchedulerGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	private Integer deepDay;
	
	private String cronString;
	
	private boolean enabled;
		
	
	@ManyToMany(mappedBy="groups", cascade={CascadeType.MERGE})
//	@JoinTable(name = "SchedulerToDevice", joinColumns = {
//			@JoinColumn(name = "idSchedulerGroup", referencedColumnName = "id") }, inverseJoinColumns = {
//					@JoinColumn(name = "idDevice", referencedColumnName = "id") })
	private List<Device> devices;
	
	@ManyToMany(cascade={CascadeType.MERGE})
	@JoinTable(name = "SchedulerToArchiveType", joinColumns = {
			@JoinColumn(name = "idSchedulerGroup", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idArhiveType", referencedColumnName = "id") })
	private List<ArchiveType> archiveTypes;
	
	
	
	public Integer getDeepDay() {
		return deepDay;
	}

	public void setDeepDay(Integer deepDay) {
		this.deepDay = deepDay;
	}

	public String getCronString() {
		return cronString;
	}

	public void setCronString(String cronString) {
		this.cronString = cronString;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public List<ArchiveType> getArchiveTypes() {
		return archiveTypes;
	}

	public void setArchiveTypes(List<ArchiveType> archiveTypes) {
		this.archiveTypes = archiveTypes;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
