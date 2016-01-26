package ru.sevenkt.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String deviceName;

	private String serialNum;

	private Group group;

}
