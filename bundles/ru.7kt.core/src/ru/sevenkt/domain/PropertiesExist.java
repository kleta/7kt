package ru.sevenkt.domain;

import java.util.List;
import java.util.Properties;

public interface PropertiesExist {
	List<Properties> getProperies() throws IllegalArgumentException, IllegalAccessException;
}
