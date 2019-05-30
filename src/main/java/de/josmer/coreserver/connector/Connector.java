package de.josmer.coreserver.connector;

import com.ibatis.common.jdbc.ScriptRunner;
import de.josmer.coreserver.base.Toolbox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Connector {

    public static final String CONFIG = "?autoReconnect=true&maxReconnects=100&serverTimezone=UTC&useSSL=true";
    private static final Logger LOGGER = LogManager.getLogger(Connector.class.getName());
    private static String url = null;
    private static String user = null;
    private static String pw = null;

    Connector() {
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(final String data) {
        if (isSet(url, data)) {
            url = data + CONFIG;
        }
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(final String data) {
        if (isSet(user, data)) {
            user = data;
        }
    }

    public static String getPw() {
        return pw;
    }

    public static void setPw(final String data) {
        if (isSet(pw, data)) {
            pw = data;
        }
    }

    private static boolean isSet(final String classData, final String data) {
        return Toolbox.isNull(classData) && !Toolbox.isNull(data) && !data.equals("");
    }

    public static void updateTables() {
        runScript("./sql/update.sql");
    }

    public static void createTables() {
        runScript("./sql/tables.sql");
    }

    private static void runScript(final String path) {
        try {
            new ScriptRunner(
                    DriverManager.getConnection(getUrl(), getUser(), getPw()),
                    false, false)
                    .runScript(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        } catch (IOException | SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
    }

}
