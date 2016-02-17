package ru.sevenkt.db.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

	@Column(unique=true)
	private String serialNum;
	
	private int deviceVersion;
	
	
	private int wMin0;

	
	private int wMin1;

	
	private int wMax12;

	
	private int wMax34;


	private int netAddress;

	
	private int formulaNum;

	
	private float tempColdWaterSetting;

	
	private float volumeByImpulsSetting1;

	
	private float volumeByImpulsSetting2;

	
	private float volumeByImpulsSetting3;

	
	private float volumeByImpulsSetting4;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "NodeToDevice", joinColumns = {
			@JoinColumn(name = "idDevice", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idNode", referencedColumnName = "id") })
	private List<Node> nodes;

}
