package com.orbis.coreserver.api.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.entities.beans.ResponseBean;
import com.orbis.coreserver.base.entities.beans.RightBean;
import com.orbis.coreserver.base.entities.beans.RightsBean;
import com.orbis.coreserver.base.interfaces.repositories.IUserRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import com.orbis.coreserver.repositories.UserRepository;
import com.orbis.coreserver.repositories.UserRightRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserRightServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
        TestUtils.setConfig();
        TestUtils.cleanTables();
        TestUtils.setServiceRights(1, UserRightService.WEB_CONTEXT_PATH);
    }

    @Before
    public void setUp() {
        TestUtils.cleanAndInsertUserRightDemoData();
        TestUtils.setServiceRights(1, UserRightService.WEB_CONTEXT_PATH);
    }

    @Test
    public void testGet() {
        UserRight expectedEntity = new UserRight();

        Optional<UserRight> optionalEntity = Factory.get(UserRightRepository.class).get(1);

        if (!optionalEntity.isPresent()) {
            fail();
        } else {
            expectedEntity = optionalEntity.get();
        }
        String response = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .path("get")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();
        UserRight resultEntity = new Gson().fromJson(response, UserRight.class);

        assertEquals(expectedEntity.getId(), resultEntity.getId());
        assertEquals(expectedEntity.getUserId(), resultEntity.getUserId());
        assertEquals(expectedEntity.getService(), resultEntity.getService());
        assertEquals(expectedEntity.getServiceRight(), resultEntity.getServiceRight());
    }

    @Test
    public void testGetRights() {
        TestUtils.saveUser("user@abce.de");
        String response = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .path("getRights")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();

        RightsBean rightsBean = new Gson().fromJson(response, RightsBean.class);

        assertEquals("user@abce.de", rightsBean.getLogin());
        assertEquals(1, rightsBean.getUserId());
        assertTrue(rightsBean.getRights().size() >= 4);
    }

    @Test
    public void testAll() {
        String response = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();
        Type listType = new TypeToken<List<UserRight>>() {
        }.getType();

        List<UserRight> result = new Gson().fromJson(response, listType);

        assertTrue(result.stream().anyMatch(u -> "GET".equals(u.getServiceRight())));
        assertTrue(result.stream().anyMatch(u -> "PUT".equals(u.getServiceRight())));
        assertTrue(result.stream().anyMatch(u -> "service".equals(u.getService())));
    }

    @Test
    public void testSave() {
        UserRight entity = new UserRight();
        entity.setService("service56");
        String result = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .post(Entity.json(entity), String.class);
        logApiRequestTime();
        assertEquals(TestUtils.OK_RESULT, result);
    }

    @Test
    public void testUpdate() {
        Optional<UserRight> optionalEntity = Factory.get(UserRightRepository.class).get(1);

        if (optionalEntity.isPresent()) {
            UserRight entity = optionalEntity.get();
            entity.setService("service78");
            String result = getTarget()
                    .path(UserRightService.WEB_CONTEXT_PATH)
                    .request(MediaType.APPLICATION_JSON)
                    .header("token", Token.get(1))
                    .put(Entity.json(entity), String.class);
            logApiRequestTime();
            assertEquals(TestUtils.OK_RESULT, result);
        } else {
            fail();
        }
    }

    @Test
    public void testUpdateRights() {
        IUserRightRepository userRightRepository = Factory.get(UserRightRepository.class);
        IUserRepository userRepository = Factory.get(UserRepository.class);
        TestUtils.saveUser("user@abce.de");
        Optional<User> optionalUser = userRepository.get(1);
        RightsBean rightsBean = new RightsBean();

        if (optionalUser.isPresent()) {
            rightsBean = userRightRepository.getRights(optionalUser.get());
            assertEquals(1, rightsBean.getUserId());
            assertEquals("user@abce.de", rightsBean.getLogin());
            assertFalse(rightsBean.getRights().stream().anyMatch(r -> "user".equals(r.getService())));
        } else {
            fail();
        }

        List<RightBean> rights = rightsBean.getRights();
        RightBean rightBean = new RightBean();
        rightBean.setServiceRight("DELETE");
        rightBean.setService("user");
        rights.add(rightBean);
        rightsBean.setRights(rights);

        String result = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .path("updateRights")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .put(Entity.json(rightsBean), String.class);
        logApiRequestTime();

        ResponseBean responseBean = new Gson().fromJson(result, ResponseBean.class);

        assertEquals("1", responseBean.getMessage());

        rightsBean = userRightRepository.getRights(optionalUser.get());

        assertEquals("user@abce.de", rightsBean.getLogin());
        assertEquals(1, rightsBean.getUserId());
        assertTrue(rightsBean.getRights().stream().anyMatch(r -> "user".equals(r.getService())));
    }

    @Test
    public void testDelete() {
        String result = getTarget()
                .path(UserRightService.WEB_CONTEXT_PATH)
                .path("delete")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .delete(String.class);
        logApiRequestTime();
        assertEquals(TestUtils.OK_RESULT, result);
    }

}
