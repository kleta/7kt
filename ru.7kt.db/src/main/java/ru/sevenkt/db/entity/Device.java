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

	private List<Group> groups;

}
