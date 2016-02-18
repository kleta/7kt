package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Measuring;
import ru.sevenkt.db.entities.MeasuringPK;

public interface MeasuringRepo extends CrudRepository<Measuring, MeasuringPK> {

}
