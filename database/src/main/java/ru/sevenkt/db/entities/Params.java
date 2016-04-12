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
	
//	@ManyToMany(cascade=CascadeType.ALL,mappedBy = "params")
//	private List<Device> devices;
	
	public Params(){
	}
	
	public Params(Parameters p){
		id=p;
		name=p.getName();
	}
	
	
}
