package de.josmer.coreserver.connector;

import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.connector.ConnectorResponse;
import de.josmer.coreserver.base.interfaces.connector.IConnector;
import de.josmer.coreserver.base.interfaces.connector.Query;
import org.junit.Test;
import utils.Factory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MySqlConnectorTest {

    @Test
    public void Tests() {
        IConnector connector = Factory.get(MySqlConnector.class);

        List<User> entities = connector.getEntities(new Query<>(EntityTypes.USER, User.class, " "));
        assertTrue(entities.isEmpty());

        User entity = connector.getEntity(new Query<>(EntityTypes.USER, User.class, " "));
        assertEquals(null, entity);

        ConnectorResponse executed = connector.update(new Query<>(EntityTypes.USER, User.class, " "));
        assertEquals(0, executed.getUpdates());
    }

}
