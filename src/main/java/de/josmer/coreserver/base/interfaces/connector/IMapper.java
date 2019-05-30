package de.josmer.coreserver.base.interfaces.connector;

import de.josmer.coreserver.base.entities.AbstractEntity;
import de.josmer.coreserver.base.enums.EntityTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapper {

    <E extends AbstractEntity> E mapTo(final EntityTypes entityTyp, final Class<E> classTyp, final ResultSet rs) throws SQLException;

}
