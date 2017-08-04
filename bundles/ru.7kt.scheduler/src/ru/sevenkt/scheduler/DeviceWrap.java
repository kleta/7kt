package ru.sevenkt.scheduler;

import java.time.LocalDate;
import java.util.Collection;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.domain.ArchiveTypes;

public class DeviceWrap {

	private Integer tryCount;

	private Device device;

	private Collection<ArchiveTypes> types;

	private LocalDate fromDate;

	private Group group;

	public DeviceWrap(Group group, Device device2, Collection<ArchiveTypes> archiveTypes, LocalDate fd, int i) {
		device=device2;
		types=archiveTypes;
		fromDate=fd;
		tryCount=i;
		this.setGroup(group);
	}

	public Integer getTryCount() {
		return tryCount;
	}

	public Device getDevice() {
		return device;
	}

	public Collection<ArchiveTypes> getTypes() {
		return types;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
