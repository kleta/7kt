package ru.sevenkt.db.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Data;
import ru.sevenkt.domain.Parameters;

@Entity
@Data
public class Params {
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
