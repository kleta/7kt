package ru.sevenkt.db.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.MeasuringPK;
import ru.sevenkt.domain.ArchiveTypes;

public interface MeasuringRepo extends CrudRepository<Measuring, MeasuringPK> {

	List<Measuring> findByDeviceAndArchiveTypeAndDateTimeBetween(Device device, ArchiveTypes archiveType,
			LocalDateTime atTime, LocalDateTime atTime2);

	void deleteByDevice(Device device);

}
