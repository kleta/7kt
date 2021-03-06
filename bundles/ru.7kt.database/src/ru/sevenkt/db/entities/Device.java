package ru.sevenkt.db.entities;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.PrivateOwned;

import ru.sevenkt.annotations.Prop;
import ru.sevenkt.domain.PropertiesExist;

@Entity
@Table(name = "Devices")
public class Device implements Serializable, PropertiesExist {
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

	private int didgitE = 3;

	private int didgitV = 2;

	private int didgitM = 2;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Set<SchedulerGroup> groups;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "DeviceToParams", joinColumns = {
			@JoinColumn(name = "idDevice", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "idParam", referencedColumnName = "id") })
	private List<Params> params;

	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
	@PrivateOwned
	private List<Report> reports;

	// @OneToMany(mappedBy="device", cascade=CascadeType.ALL)
	// @PrivateOwned
	// private List<Measuring> measurings;

	@OneToOne(cascade = CascadeType.ALL)
	@PrivateOwned
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public int getDidgitE() {
		return didgitE;
	}

	public void setDidgitE(int didgitE) {
		this.didgitE = didgitE;
	}

	public int getDidgitV() {
		return didgitV;
	}

	public void setDidgitV(int didgitV) {
		this.didgitV = didgitV;
	}

	public int getDidgitM() {
		return didgitM;
	}

	public void setDidgitM(int didgitM) {
		this.didgitM = didgitM;
	}

	public List<Report> getReports() {
		if (reports == null)
			reports = new ArrayList<>();
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public int getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(int deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public int getwMin0() {
		return wMin0;
	}

	public void setwMin0(int wMin0) {
		this.wMin0 = wMin0;
	}

	public int getwMin1() {
		return wMin1;
	}

	public void setwMin1(int wMin1) {
		this.wMin1 = wMin1;
	}

	public int getwMax12() {
		return wMax12;
	}

	public void setwMax12(int wMax12) {
		this.wMax12 = wMax12;
	}

	public int getwMax34() {
		return wMax34;
	}

	public void setwMax34(int wMax34) {
		this.wMax34 = wMax34;
	}

	public boolean isControlPower() {
		return controlPower;
	}

	public void setControlPower(boolean controlPower) {
		this.controlPower = controlPower;
	}

	public int getNetAddress() {
		return netAddress;
	}

	public void setNetAddress(int netAddress) {
		this.netAddress = netAddress;
	}

	public int getFormulaNum() {
		return formulaNum;
	}

	public void setFormulaNum(int formulaNum) {
		this.formulaNum = formulaNum;
	}

	public float getTempColdWaterSetting() {
		return tempColdWaterSetting;
	}

	public void setTempColdWaterSetting(float tempColdWaterSetting) {
		this.tempColdWaterSetting = tempColdWaterSetting;
	}

	public float getVolumeByImpulsSetting1() {
		return volumeByImpulsSetting1;
	}

	public void setVolumeByImpulsSetting1(float volumeByImpulsSetting1) {
		this.volumeByImpulsSetting1 = volumeByImpulsSetting1;
	}

	public float getVolumeByImpulsSetting2() {
		return volumeByImpulsSetting2;
	}

	public void setVolumeByImpulsSetting2(float volumeByImpulsSetting2) {
		this.volumeByImpulsSetting2 = volumeByImpulsSetting2;
	}

	public float getVolumeByImpulsSetting3() {
		return volumeByImpulsSetting3;
	}

	public void setVolumeByImpulsSetting3(float volumeByImpulsSetting3) {
		this.volumeByImpulsSetting3 = volumeByImpulsSetting3;
	}

	public float getVolumeByImpulsSetting4() {
		return volumeByImpulsSetting4;
	}

	public void setVolumeByImpulsSetting4(float volumeByImpulsSetting4) {
		this.volumeByImpulsSetting4 = volumeByImpulsSetting4;
	}

	// public List<SchedulerGroup> getNodes() {
	// return groups;
	// }
	//
	// public void setGroups(List<SchedulerGroup> nodes) {
	// this.groups = nodes;
	// }

	public List<Params> getParams() {
		return params;
	}

	public void setParams(List<Params> params) {
		this.params = params;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Properties> getProperies() throws IllegalArgumentException, IllegalAccessException {
		List<Properties> list = new ArrayList<>();
		Field[] filds = getClass().getDeclaredFields();
		for (Field field : filds) {
			if (field.isAnnotationPresent(Prop.class)) {
				Properties prop = new Properties();
				String key = field.getAnnotation(Prop.class).name();
				Object value = field.get(this);
				prop.put(key, value);
				list.add(prop);
			}
		}
		return list;
	}

	public Device(Device device) {
		this.deviceName = device.getDeviceName() + "";
		this.deviceVersion = device.getDeviceVersion();
		this.formulaNum = device.getFormulaNum();
		this.id = device.getId();
		this.netAddress = device.getNetAddress();
		if (device.getGroups() != null) {
			groups = new HashSet<>();
			groups.addAll(device.getGroups());
		}
		this.serialNum = device.getSerialNum() + "";
		this.tempColdWaterSetting = device.getTempColdWaterSetting();
		this.volumeByImpulsSetting1 = device.getVolumeByImpulsSetting1();
		this.volumeByImpulsSetting2 = device.getVolumeByImpulsSetting2();
		this.volumeByImpulsSetting3 = device.getVolumeByImpulsSetting3();
		this.volumeByImpulsSetting4 = device.getVolumeByImpulsSetting4();
		this.wMax12 = device.getwMax12();
		this.wMax34 = device.getwMax34();
		this.wMin0 = device.getwMin0();
		this.wMin1 = device.getwMin1();
		this.controlPower = device.controlPower;
		this.didgitE = device.didgitE;
		this.didgitV = device.didgitV;
		this.didgitM = device.didgitM;
		if (device.getParams() != null) {
			params = new ArrayList<>();
			params.addAll(device.getParams());
		}
		this.setReports(device.getReports());
	}

	public Device() {

	}

	public Set<SchedulerGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<SchedulerGroup> groups) {
		this.groups = groups;
	}

	// public List<Measuring> getMeasurings() {
	// return measurings;
	// }
	//
	//
	// public void setMeasurings(List<Measuring> measurings) {
	// this.measurings = measurings;
	// }
}
