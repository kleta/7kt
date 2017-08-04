package ru.sevenkt.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.SchedulerGroup;

public interface SchedulerGroupRepo extends CrudRepository<SchedulerGroup, Long> {

	List<SchedulerGroup> findByDevices(Device device);

}
