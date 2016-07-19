package ru.sevenkt.db.repositories;

import org.springframework.data.repository.CrudRepository;

import ru.sevenkt.db.entities.Node;

public interface NodeRepo extends CrudRepository<Node, Long> {

}
