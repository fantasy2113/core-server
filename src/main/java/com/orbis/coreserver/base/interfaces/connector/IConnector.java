package com.orbis.coreserver.base.interfaces.connector;

import com.orbis.coreserver.base.entities.AbstractEntity;

import java.util.List;

public interface IConnector {

    <E extends AbstractEntity> List<E> getEntities(Query<E> query);

    <E extends AbstractEntity> E getEntity(Query<E> query);

    <E extends AbstractEntity> ConnectorResponse update(Query<E> query);
}
