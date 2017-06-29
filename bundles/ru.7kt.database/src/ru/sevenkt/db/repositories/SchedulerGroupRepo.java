package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.SchedulerGroup;

public interface SchedulerGroupRepo extends CrudRepository<SchedulerGroup, Long> {

}
