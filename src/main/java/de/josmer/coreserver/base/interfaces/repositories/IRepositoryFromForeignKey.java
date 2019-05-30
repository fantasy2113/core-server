package de.josmer.coreserver.base.interfaces.repositories;

import de.josmer.coreserver.base.entities.AbstractEntity;

import java.util.List;

public interface IRepositoryFromForeignKey<E extends AbstractEntity> {

    List<E> getAll(int foreignKey);
}
