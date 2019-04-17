package com.orbis.coreserver.api.services;

import com.orbis.coreserver.Run;
import org.junit.AfterClass;
import utils.TestUtils;

import javax.ws.rs.client.WebTarget;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ServiceTesting {

    private static final Logger LOGGER = Logger.getLogger(ServiceTesting.class.getName());
    private final WebTarget target;
    private long timeStart;

    protected ServiceTesting() {
        this.target = TestUtils.getClient();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUtils.setConfig();
        TestUtils.cleanTables();
        Run.shutdown();
    }

    protected void logApiRequestTime() {
        long timeEnd = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "Time: {0} ms", timeEnd - this.timeStart);
    }

    protected WebTarget getTarget() {
        this.timeStart = System.currentTimeMillis();
        return target;
    }
}
