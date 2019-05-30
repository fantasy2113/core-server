package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.AbstractEntity;
import de.josmer.coreserver.base.interfaces.connector.ConnectorResponse;

import java.util.List;
import java.util.Optional;

public interface IRepository<E extends AbstractEntity> {

    Optional<E> get(int id);

    List<E> getAll();

    ConnectorResponse save(E entity);

    ConnectorResponse update(E entity);

    ConnectorResponse delete(int id);

    String getTableName();
}
