package com.orbis.coreserver.connector.mapper;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.connector.Connector;
import org.junit.Test;
import utils.TestUtils;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MapperTest {

    @Test
    public void mapTo1() {
        TestUtils.setConfig();
        try (Connection conn = DriverManager.getConnection(Connector.getUrl(), Connector.getUser(), Connector.getPw())) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            new Mapper().mapTo(EntityTypes.USER, null, rs);
            fail();
        } catch (Exception ex) {
            assertEquals(ex.getClass(), SQLException.class);
        }
    }

    @Test
    public void mapTo2() {
        TestUtils.setConfig();
        try (Connection conn = DriverManager.getConnection(Connector.getUrl(), Connector.getUser(), Connector.getPw())) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            new Mapper().mapTo(EntityTypes.USER, User.class, null);
            fail();
        } catch (Exception ex) {
            assertEquals(ex.getClass(), SQLException.class);
        }
    }

    @Test
    public void mapTo3() {
        TestUtils.setConfig();
        try (Connection conn = DriverManager.getConnection(Connector.getUrl(), Connector.getUser(), Connector.getPw())) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            new Mapper().mapTo(EntityTypes.USER, null, null);
            fail();
        } catch (Exception ex) {
            assertEquals(ex.getClass(), SQLException.class);
        }
    }

    @Test
    public void mapTo4() {
        TestUtils.setConfig();
        try (Connection conn = DriverManager.getConnection(Connector.getUrl(), Connector.getUser(), Connector.getPw())) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM user");
            new Mapper().mapTo(null, null, null);
            fail();
        } catch (Exception ex) {
            assertEquals(ex.getClass(), SQLException.class);
        }
    }
}
