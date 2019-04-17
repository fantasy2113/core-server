package com.orbis.coreserver.base.interfaces.connector;

import com.orbis.coreserver.base.entities.AbstractEntity;
import com.orbis.coreserver.base.enums.EntityTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public final class Query<E extends AbstractEntity> {

    private static final Logger LOGGER = LogManager.getLogger(Query.class.getName());
    private final String statement;
    private final EntityTypes entityTyp;
    private final Class<E> classTyp;

    public Query(final EntityTypes entityTyp, final Class<E> classTyp, final String statement) {
        this.entityTyp = entityTyp;
        this.classTyp = classTyp;
        this.statement = check(statement);
    }

    public String getStatement() {
        return this.statement;
    }

    public EntityTypes getEntityTyp() {
        return entityTyp;
    }

    public Class<E> getClassTyp() {
        return classTyp;
    }

    private String check(final String statement) {
        if (isNotValid(statement)) {
            LOGGER.info("forbidden sql statement");
            return "- empty -";
        }
        return statement;
    }

    private boolean isNotValid(final String statement) {
        final String lowerCaseQuery = statement.toLowerCase(Locale.ENGLISH);
        return lowerCaseQuery.contains(" drop ") || lowerCaseQuery.contains(" truncate ");
    }
}
