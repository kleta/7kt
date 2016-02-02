package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.ArchiveType;

public interface ArchiveTypeRepo extends CrudRepository<ArchiveType,Long> {

}
