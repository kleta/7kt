package ru.sevenkt.db.entities;

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

import lombok.Data;
import ru.sevenkt.annotations.Prop;

@Entity
@Data
public class Device {

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
}
