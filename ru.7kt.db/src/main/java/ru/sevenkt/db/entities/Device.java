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

import lombok.Data;

@Entity
@Data
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String deviceName;

	private String serialNum;

	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(
      name="DevicesGroups",
      joinColumns={@JoinColumn(name="idDevice", referencedColumnName="id")},
      inverseJoinColumns={@JoinColumn(name="idGroup", referencedColumnName="id")})
	private List<Group> groups;

}
