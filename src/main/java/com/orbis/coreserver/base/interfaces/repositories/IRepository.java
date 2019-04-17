package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.AbstractEntity;
import com.orbis.coreserver.base.interfaces.connector.ConnectorResponse;

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
