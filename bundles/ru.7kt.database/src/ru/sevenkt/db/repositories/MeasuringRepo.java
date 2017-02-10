package ru.sevenkt.db.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.MeasuringPK;
import ru.sevenkt.domain.ArchiveTypes;
import ru.sevenkt.domain.Parameters;

public interface MeasuringRepo extends CrudRepository<Measuring, MeasuringPK> {

	List<Measuring> findByDeviceAndArchiveTypeAndDateTimeBetween(Device device, ArchiveTypes archiveType,
			LocalDateTime atTime, LocalDateTime atTime2);

	void deleteByDevice(Device device);

	void deleteByDeviceAndDateTimeBetween(Device device, LocalDateTime start, LocalDateTime end);

	Measuring findTopByDeviceOrderByDateTimeAsc(Device device);

	Measuring findTopByDeviceOrderByDateTimeDesc(Device device);

	@Query("select sum(m.value) from Measuring  m where m.parameter =:parameter and m.device= :device and m.archiveType=:at and m.dateTime between :dateTimeFrom and :dateTimeTo")
	Double getSumHoursValue(@Param("parameter") Parameters parameter, @Param("device") Device device,
			@Param("at") ArchiveTypes at, @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
			@Param("dateTimeTo") LocalDateTime dateTimeTo);

	Measuring findByParameterAndDeviceAndArchiveTypeAndDateTime(Parameters parameter, Device device, ArchiveTypes day,
			LocalDateTime minusDays);

}
