package com.orbis.coreserver.connector;

import com.orbis.coreserver.base.entities.AbstractEntity;
import com.orbis.coreserver.base.interfaces.connector.ConnectorResponse;
import com.orbis.coreserver.base.interfaces.connector.IConnector;
import com.orbis.coreserver.base.interfaces.connector.IMapper;
import com.orbis.coreserver.base.interfaces.connector.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlConnector extends Connector implements IConnector {

    private static final Logger LOGGER = LogManager.getLogger(MySqlConnector.class.getName());
    private final IMapper mapper;

    @Inject
    public MySqlConnector(@Named("Mapper") IMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <E extends AbstractEntity> List<E> getEntities(final Query<E> query) {
        List<E> returnEntities = new ArrayList<>();
        LOGGER.info(query.getStatement());
        try (Connection conn = DriverManager.getConnection(getUrl(), getUser(), getPw());
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query.getStatement())) {
            while (rs.next()) {
                returnEntities.add(mapper.mapTo(query.getEntityTyp(), query.getClassTyp(), rs));
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
        return returnEntities;
    }

    @Override
    public <E extends AbstractEntity> E getEntity(final Query<E> query) {
        LOGGER.info(query.getStatement());
        try (Connection conn = DriverManager.getConnection(getUrl(), getUser(), getPw());
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query.getStatement())) {
            if (rs.next()) {
                return mapper.mapTo(query.getEntityTyp(), query.getClassTyp(), rs);
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
        return null;
    }

    @Override
    public <E extends AbstractEntity> ConnectorResponse update(final Query<E> query) {
        ConnectorResponse connectorResponse = new ConnectorResponse();
        LOGGER.info(query.getStatement());
        try (Connection conn = DriverManager.getConnection(getUrl(), getUser(), getPw());
             Statement st = conn.createStatement()) {
            connectorResponse.setUpdates(st.executeUpdate(query.getStatement(), Statement.RETURN_GENERATED_KEYS));
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    connectorResponse.setEntityId(rs.getInt(1));
                    LOGGER.info("Generated key: " + connectorResponse.getEntityId());
                }
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
        return connectorResponse;
    }
}
