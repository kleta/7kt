package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Params;
import ru.sevenkt.domain.Parameters;

public interface ParamsRepo extends CrudRepository<Params, Parameters> {

}
