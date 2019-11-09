package de.josmer.coreserver;

import de.josmer.coreserver.connector.Connector;
import de.josmer.coreserver.connector.MySqlConnector;
import org.junit.Test;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

public class RunTest {

    @Test
    public void testSetConfig() {
        TestUtils.setConfig();

        assertEquals("julian", MySqlConnector.getUser());
        assertEquals("qw999", MySqlConnector.getPw());
        assertEquals("jdbc:mysql://localhost:3306/coreserver_testing" + Connector.CONFIG, Connector.getUrl());
        assertEquals(TestUtils.SERVER_URL, CoreServer.getUrl());
    }
}
