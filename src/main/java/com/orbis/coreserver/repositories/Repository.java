package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.entities.AbstractEntity;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.interfaces.connector.Columns;
import com.orbis.coreserver.base.interfaces.connector.ConnectorResponse;
import com.orbis.coreserver.base.interfaces.connector.IConnector;
import com.orbis.coreserver.base.interfaces.connector.Query;
import com.orbis.coreserver.base.interfaces.repositories.IRepository;

import java.util.List;
import java.util.Optional;

public abstract class Repository<E extends AbstractEntity> implements IRepository<E> {

    protected static final String LIMIT_1 = " LIMIT 1 ";
    protected static final String SELECT_FROM = "SELECT * FROM ";
    protected static final String SELECT_FROM_PW = "SELECT " + Columns.PW + " FROM ";
    protected static final String UPDATE = "UPDATE ";
    protected static final String INSERT_INTO = "INSERT INTO ";
    protected static final String VALUES = " VALUES ";
    protected static final String WHERE = " WHERE ";
    protected static final String AND = " AND ";
    protected static final String SET = " SET ";
    protected static final String EQ = "=";
    protected static final String END = ";";
    protected static final String APO = "'";
    protected static final String COL = ", ";
    protected static final String OPEN = " ( ";
    protected static final String CLOSE = " ) ";
    protected static final String DELETE_FROM = "DELETE FROM ";

    private final IConnector connector;
    private final String table;
    private final EntityTypes entityTyp;
    private final Class<E> classTyp;

    protected Repository(final IConnector connector, final EntityTypes entityTyp, final Class<E> classTyp) {
        this.connector = connector;
        this.entityTyp = entityTyp;
        this.classTyp = classTyp;
        this.table = entityTyp.toString();
    }

    @Override
    public Optional<E> get(final int id) {
        final String statement = SELECT_FROM + getTableName() + WHERE + Columns.ID + EQ + id + LIMIT_1 + END;
        return Optional.ofNullable(entity(statement));
    }

    @Override
    public List<E> getAll() {
        final String statement = SELECT_FROM + getTableName() + END;
        return entities(statement);
    }

    @Override
    public ConnectorResponse delete(final int id) {
        final String statement = DELETE_FROM + getTableName() + WHERE + Columns.ID + EQ + id + LIMIT_1 + END;
        return execute(statement);
    }

    @Override
    public final String getTableName() {
        return table;
    }

    protected final ConnectorResponse execute(final String statement) {
        return getConnector().update(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

    protected final E entity(final String statement) {
        return getConnector().getEntity(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

    protected final List<E> all(final int foreignKey, final String foreignKeyColumn) {
        final String statement = SELECT_FROM + getTableName() + WHERE + foreignKey + EQ + foreignKeyColumn + END;
        return entities(statement);
    }

    protected final String getEqId(final int id) {
        return WHERE + Columns.ID + EQ + id + END;
    }

    protected final IConnector getConnector() {
        return connector;
    }

    protected final EntityTypes getEntityTyp() {
        return entityTyp;
    }

    protected final Class<E> getClassTyp() {
        return classTyp;
    }

    protected int parseBoolean(final boolean bool) {
        return bool ? 1 : 0;
    }

    protected List<E> entities(final String statement) {
        return getConnector().getEntities(new Query<>(getEntityTyp(), getClassTyp(), statement));
    }

}
