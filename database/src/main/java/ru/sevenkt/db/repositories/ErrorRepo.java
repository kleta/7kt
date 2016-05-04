package ru.sevenkt.db.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.ErrorPK;
import ru.sevenkt.domain.ArchiveTypes;

public interface ErrorRepo extends CrudRepository<Error, ErrorPK> {

	List<Error> findByDeviceAndArchiveTypeAndDateTimeBetween(Device device, ArchiveTypes archiveType,
			LocalDateTime atTime, LocalDateTime atTime2);

	void deleteByDeviceAndDateTimeBetween(Device device, LocalDateTime min, LocalDateTime plusMonths);


}
