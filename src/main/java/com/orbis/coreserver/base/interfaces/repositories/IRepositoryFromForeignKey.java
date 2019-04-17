package com.orbis.coreserver.base.interfaces.repositories;

import com.orbis.coreserver.base.entities.AbstractEntity;

import java.util.List;

public interface IRepositoryFromForeignKey<E extends AbstractEntity> {

    List<E> getAll(int foreignKey);
}
