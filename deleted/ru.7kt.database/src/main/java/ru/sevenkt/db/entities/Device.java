package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import ru.sevenkt.annotations.Prop;

@Entity
@Table(name="Devices")
@Data
public class Device implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2048945841888190110L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Prop(name = "Наименование")
	private String deviceName;

	@Column(unique = true)
	@Prop(name = "Серийный номер")
	private String serialNum;

	@Prop(name = "Версия устройства")
	private int deviceVersion;

	private int wMin0;

	private int wMin1;

	private int wMax12;

	private int wMax34;
	
	private boolean controlPower;

	@Prop(name = "Сетевой адрес")
	private int netAddress;

	@Prop(name = "Схема")
	private int formulaNum;

	@Prop(name = "Температура хол. воды(°C)")
	private float tempColdWaterSetting;

	@Prop(name = "Вес импульса V1(л/имп)")
	private float volumeByImpulsSetting1;

	@Prop(name = "Вес импульса V2(л/имп)")
	private float volumeByImpulsSetting2;

	@Prop(name = "Вес импульса V3(л/имп)")
	private float volumeByImpulsSetting3;

	@Prop(name = "Вес импульса V4(л/имп)")
	private float volumeByImpulsSetting4;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "NodeToDevice", joinColumns = {
			@JoinColumn(name = "idDevice", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idNode", referencedColumnName = "id") })
	private List<Node> nodes;
	
	@ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name = "DeviceToParams", joinColumns = {
			@JoinColumn(name = "idDevice", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idParam", referencedColumnName = "id") })
	private List<Params> params;

	public List<Properties> getProperies() throws IllegalArgumentException, IllegalAccessException {
		List<Properties> list=new ArrayList<>();
		Field[] filds = getClass().getDeclaredFields();
		for (Field field : filds) {
			if (field.isAnnotationPresent(Prop.class)) {
				Properties prop=new Properties();
				String key = field.getAnnotation(Prop.class).name();
				Object value = field.get(this);
				prop.put(key, value);
				list.add(prop);
			}
		}
		return list;
	}

	public Device(Device device) {
		this.deviceName=device.getDeviceName()+"";
		this.deviceVersion=new Integer(device.getDeviceVersion());
		this.formulaNum=new Integer(device.getFormulaNum());
		this.id=new Integer(device.getId());
		this.netAddress=device.getNetAddress();
		nodes=new ArrayList<>();
		nodes.addAll(device.getNodes());
		this.serialNum=device.getSerialNum()+"";
		this.tempColdWaterSetting=new Float(device.getTempColdWaterSetting());
		this.volumeByImpulsSetting1=new Float(device.getVolumeByImpulsSetting1());
		this.volumeByImpulsSetting2=new Float(device.getVolumeByImpulsSetting2());
		this.volumeByImpulsSetting3=new Float(device.getVolumeByImpulsSetting3());
		this.volumeByImpulsSetting4=new Float(device.getVolumeByImpulsSetting4());
		this.wMax12=new Integer(device.getWMax12());
		this.wMax34=new Integer(device.getWMax34());
		this.wMin0=new Integer(device.getWMin0());
		this.wMin1=new Integer(device.getWMin1());
		this.controlPower=device.controlPower;
		
		params=new ArrayList<>();
		params.addAll(device.getParams());	
	}

	public Device() {
		
	}
}
