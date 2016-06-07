package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Device;

public interface DeviceRepo extends CrudRepository<Device, Long> {

	Device findBySerialNum(String string);

}
