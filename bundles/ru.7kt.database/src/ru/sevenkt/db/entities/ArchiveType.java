package ru.sevenkt.db.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import ru.sevenkt.domain.ArchiveTypes;

@Entity
public class ArchiveType {
	@Id
	private ArchiveTypes id;
	
	private String name;

	public ArchiveTypes getId() {
		return id;
	}
	
//	@ManyToMany(mappedBy="archiveTypes")
//	private List<SchedulerGroup> groups;

	public void setId(ArchiveTypes id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArchiveType(ArchiveTypes id) {
		this.id = id;
		this.name = id.getName();
	}

	public ArchiveType() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArchiveType other = (ArchiveType) obj;
		if (id != other.id)
			return false;
		return true;
	}

//	public List<SchedulerGroup> getGroups() {
//		return groups;
//	}
//
//	public void setGroups(List<SchedulerGroup> groups) {
//		this.groups = groups;
//	}
	
	
	
	
}
