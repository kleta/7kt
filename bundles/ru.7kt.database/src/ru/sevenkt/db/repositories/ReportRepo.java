package ru.sevenkt.db.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;
import ru.sevenkt.db.entities.Report;

public interface ReportRepo extends CrudRepository<Report, Integer> {
	List<Report> findByDevice(Device device);
}
