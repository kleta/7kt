package ru.sevenkt.db.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Journal;
import ru.sevenkt.db.entities.JournalPK;

public interface JournalRepo extends CrudRepository<Journal, JournalPK> {

	List<Journal> findByDeviceAndDateTimeBetween(Device device, LocalDate startDate, LocalDate endDate);

	List<Journal> findByDevice(Device device);

}
