package de.josmer.coreserver.api.services.developer;

import de.josmer.coreserver.api.services.ServiceTesting;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TestUtils;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertTrue;

public class ServerServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
        TestUtils.setConfig();
    }

    @Test
    public void index() {
        assertTrue(!responseFromPath("index.html").isEmpty());
    }

    @Test
    public void appLogs() {
        assertTrue(!responseFromPath("logs.html").isEmpty());
    }

    private String responseFromPath(final String path) {
        String response = getTarget()
                .path(ServerService.WEB_CONTEXT_PATH)
                .path(path)
                .request(MediaType.TEXT_HTML)
                .get(String.class);
        logApiRequestTime();
        return response;
    }
}
