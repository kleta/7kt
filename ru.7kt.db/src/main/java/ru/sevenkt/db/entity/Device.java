package ru.sevenkt.db.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String deviceName;

	private String serialNum;

	@ManyToMany
	@JoinTable(
      name="DevicesGroups",
      joinColumns={@JoinColumn(name="idDevice", referencedColumnName="id")},
      inverseJoinColumns={@JoinColumn(name="idGroup", referencedColumnName="id")})
	private List<Group> groups;

}
