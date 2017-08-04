package ru.sevenkt.scheduler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import ru.sevenkt.db.entities.ArchiveType;
import ru.sevenkt.db.entities.SchedulerGroup;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.PropertiesExist;

public class Group implements PropertiesExist{
	
	private String name;
	
	private String state;
	
	private Collection<ArchiveTypes> archiveTypes;
	
	
	private SchedulerGroup schedulerGroup;
	
	private final PropertyChangeSupport changeSupport =
            new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener
            listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener
            listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
            Object newValue) {
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
	
	

	public Group(SchedulerGroup schedulerGroup) {
		this.schedulerGroup = schedulerGroup;
		name=schedulerGroup.getName();
		state="Ожидание";
		archiveTypes=new ArrayList<>();
		for(ArchiveType a:schedulerGroup.getArchiveTypes()){
			archiveTypes.add(a.getId());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Collection<ArchiveTypes> getArchiveTypes() {
		return archiveTypes;
	}

	public void setArchiveTypes(Set<ArchiveTypes> archiveTypes) {
		this.archiveTypes = archiveTypes;
	}

	public int getDeviceCount() {
		return schedulerGroup.getDevices().size();
	}

	
	
	public List<Properties> getProperies(){
		CronDescriptor descriptor = CronDescriptor.instance(new Locale("ru", "RU"));
		CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
		CronParser parser = new CronParser(cronDefinition);
		String cronString = schedulerGroup.getCronString();
		String description = descriptor.describe(parser.parse(cronString));
		List<Properties> list=new ArrayList<>();
		String archStr="";
		for(ArchiveTypes a:archiveTypes){
			archStr+=a.getName()+" ";
		}
		Properties prop=new Properties();
		prop.setProperty("Наименование", name);
		list.add(prop);
		prop=new Properties();
		prop.setProperty("Запуск", description);
		list.add(prop);
		prop=new Properties();
		prop.setProperty("Архивы", archStr);
		list.add(prop);
		prop=new Properties();
		prop.setProperty("Дней считывания", schedulerGroup.getDeepDay()+"");
		list.add(prop);
		return list;
	}

	public int getDeepDays() {
		return schedulerGroup.getDeepDay();
	}

	

	public SchedulerGroup getSchedulerGroup() {
		schedulerGroup.setName(getName());
		schedulerGroup.setArchiveTypes(new ArrayList<>());
		for (ArchiveTypes archiveType : archiveTypes) {
			schedulerGroup.getArchiveTypes().add(new ArchiveType(archiveType));
		}
		schedulerGroup.setDeepDay(getDeepDays());
		return schedulerGroup;
	}

	public void setSchedulerGroup(SchedulerGroup schedulerGroup) {
		this.schedulerGroup = schedulerGroup;
	}

	@Override
	public String toString() {
		return "Group [name=" + name + ", state=" + state + ", archiveTypes=" + archiveTypes + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schedulerGroup == null) ? 0 : schedulerGroup.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (schedulerGroup.getId() == null) {
			if (other.schedulerGroup.getId() != null)
				return false;
		} else if (!schedulerGroup.getId().equals(other.schedulerGroup.getId()))
			return false;
		return true;
	}

	public void setEnabled(boolean val) {
		schedulerGroup.setEnabled(val);		
	}
	
}
