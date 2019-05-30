package de.josmer.coreserver;

import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.api.services.*;
import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.config.CoreServerConfig;
import de.josmer.coreserver.config.CoreServerExecutor;
import de.josmer.coreserver.config.RootUser;
import de.josmer.coreserver.connector.Connector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Run {

    private static final Logger LOGGER = LogManager.getLogger(Run.class.getName());
    private static CoreServer coreServer = null;
    private static boolean isDeveloperMode = false;

    public static void main(String[] args) throws IOException, InterruptedException {

        if (isNoArgs(args)) {
            LOGGER.info("try to read args from file...");
            if (isReadArgsFails()) {
                LOGGER.info("no args found");
                return;
            }
        } else {
            LOGGER.info("read args from command line...");
        }

        configure(args);
        start(false);
    }

    public static void start(final boolean isJUnit) throws InterruptedException, IOException {
        updateDatabase(isJUnit);
        coreServer = new CoreServer();
        coreServer.start(false, new CoreServerConfig(isDeveloperMode));
        if (!isJUnit) {
            new CoreServerExecutor().startAll();
            LOGGER.info("url: " + CoreServer.getUrl());
            LOGGER.info("info: " + CoreServer.getUrl() + "/server/index.html");
            LOGGER.info("logs: " + CoreServer.getUrl() + "/server/logs.html");
            Thread.currentThread().join();
        }
    }

    private static void updateDatabase(final boolean isJUnit) {
        Connector.updateTables();
        Connector.createTables();
        if (!isJUnit) {
            RootUser rootUser = new RootUser(
                    UserRightService.WEB_CONTEXT_PATH,
                    UserService.WEB_CONTEXT_PATH,
                    CustomerService.WEB_CONTEXT_PATH,
                    CustomerContactService.WEB_CONTEXT_PATH,
                    CustomerAddressService.WEB_CONTEXT_PATH
            );
            rootUser.create();
        }
    }

    public static void shutdown() {
        LOGGER.info("shutdown server");
        coreServer.shutdown();
    }

    private static boolean isNoArgs(String[] args) {
        return args == null || args.length == 0;
    }

    private static void activateDeveloperMode() {
        if (Toolbox.isNull(coreServer)) {
            isDeveloperMode = true;
        }
    }

    public static void configure(final String[] args) {
        Token.init();
        String delimiter = ":=";
        for (String arg : args) {
            if (arg.contains("sql_user" + delimiter)) {
                Connector.setUser(arg.split(delimiter)[1]);
            } else if (arg.contains("sql_pwd" + delimiter)) {
                Connector.setPw(arg.split(delimiter)[1]);
            } else if (arg.contains("sql_url" + delimiter)) {
                Connector.setUrl(arg.split(delimiter)[1]);
            } else if (arg.contains("server_url" + delimiter)) {
                CoreServer.setUrl(arg.split(delimiter)[1]);
            } else if (arg.contains("development")) {
                activateDeveloperMode();
                LOGGER.info("developer mode is activated");
            } else {
                LOGGER.info("some arg failed");
            }
        }
    }

    private static boolean isReadArgsFails() {
        try {
            final String[] args = new String(Files.readAllBytes(Paths.get("./run.args")), StandardCharsets.UTF_8).split(" ");
            configure(args);
            return false;
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
        return true;
    }
}
