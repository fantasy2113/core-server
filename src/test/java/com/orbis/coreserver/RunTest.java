package com.orbis.coreserver;

import com.orbis.coreserver.connector.Connector;
import com.orbis.coreserver.connector.MySqlConnector;
import org.junit.Test;
import utils.TestUtils;

import static org.junit.Assert.assertEquals;

public class RunTest {

    @Test
    public void testSetConfig() {
        TestUtils.setConfig();

        assertEquals("core", MySqlConnector.getUser());
        assertEquals("qw999", MySqlConnector.getPw());
        assertEquals("jdbc:mysql://localhost:3306/coreserver_testing" + Connector.CONFIG, Connector.getUrl());
        assertEquals(TestUtils.SERVER_URL, CoreServer.getUrl());
    }
}
