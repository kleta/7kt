package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Error;
import ru.sevenkt.db.entities.ErrorPK;

public interface ErrorRepo extends CrudRepository<Error, ErrorPK> {


}
