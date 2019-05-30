package de.josmer.coreserver.connector;

import de.josmer.coreserver.base.interfaces.connector.IUserConnector;
import de.josmer.coreserver.base.interfaces.connector.Query;
import org.junit.Before;
import org.junit.Test;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

public class UserConnectorTest {

    @Before
    public void setUp() {
        TestUtils.setConfig();
    }

    @Test
    public void hashedPw() {
        IUserConnector userConnector = new UserConnector();
        assertEquals("", userConnector.hashedPassword(new Query<>(null, null, "")));
        assertEquals("", userConnector.hashedPassword(new Query<>(null, null, "SELECT pw FROM user WHERE id=0 LIMIT 1 ;")));
    }
}
