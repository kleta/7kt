package ru.sevenkt.db.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import ru.sevenkt.domain.Parameters;

@Entity
public class Params {
	public Parameters getId() {
		return id;
	}

	public void setId(Parameters id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	private Parameters id;
	
	private String name;
	
	
	public Params(){
	}
	
	public Params(Parameters p){
		id=p;
		name=p.getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Params other = (Params) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	
}
