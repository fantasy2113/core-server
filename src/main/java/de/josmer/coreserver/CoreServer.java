package de.josmer.coreserver;

import de.josmer.coreserver.base.Toolbox;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public final class CoreServer {

    private static String url = null;
    private HttpServer server = null;

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String data) {
        if (Toolbox.isNull(url)) {
            url = data;
        }

    }

    public void start(final boolean secure, final ResourceConfig resourceConfig) throws IOException {
        if (secure) {
            final String KEYSTORE_LOC = "./keystore_server";
            final String KEYSTORE_PASS = "keystorePass";
            final String TRUSTSTORE_LOC = "./truststore_server";
            final String TRUSTSTORE_PASS = "truststorePass";

            SSLContextConfigurator sslContext = new SSLContextConfigurator();
            sslContext.setKeyStoreFile(KEYSTORE_LOC);
            sslContext.setKeyStorePass(KEYSTORE_PASS);
            sslContext.setTrustStoreFile(TRUSTSTORE_LOC);
            sslContext.setTrustStorePass(TRUSTSTORE_PASS);

            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), resourceConfig, true,
                    new SSLEngineConfigurator(sslContext, false, false, false));
        } else {
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), resourceConfig, false);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));

        server.start();
    }

    public void shutdown() {
        server.shutdownNow();
    }
}
