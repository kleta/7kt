package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Parameter;

public interface ParameterRepo extends CrudRepository<Parameter, Long> {

}
