package com.orbis.coreserver.connector;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.interfaces.connector.Columns;
import com.orbis.coreserver.base.interfaces.connector.IUserConnector;
import com.orbis.coreserver.base.interfaces.connector.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserConnector extends Connector implements IUserConnector {

    private static final Logger LOGGER = LogManager.getLogger(UserConnector.class.getName());

    @Override
    public <E extends User> String hashedPassword(final Query<E> query) {
        LOGGER.info(query.getStatement());
        try (Connection conn = DriverManager.getConnection(getUrl(), getUser(), getPw());
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query.getStatement())) {
            if (rs.next()) {
                return rs.getString(Columns.PW);
            }
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
        return "";
    }
}
