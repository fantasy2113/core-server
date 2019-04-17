package com.orbis.coreserver.api.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.beans.LoginBean;
import com.orbis.coreserver.base.interfaces.repositories.IUserRepository;
import com.orbis.coreserver.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import utils.Factory;
import utils.TestUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
        TestUtils.setConfig();
        TestUtils.cleanTables();
        TestUtils.setServiceRights(1, UserService.WEB_CONTEXT_PATH);
    }

    @Before
    public void setUp() {
        TestUtils.cleanAndInsertUserDemoData();
    }

    @Test
    public void testGet() {
        User expectedEntity = new User();

        Optional<User> optionalEntity = Factory.get(UserRepository.class).get(1);

        if (!optionalEntity.isPresent()) {
            fail();
        } else {
            expectedEntity = optionalEntity.get();
        }
        String response = getTarget()
                .path(UserService.WEB_CONTEXT_PATH)
                .path("get")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();
        User resultEntity = new Gson().fromJson(response, User.class);

        assertEquals(expectedEntity.getId(), resultEntity.getId());
        assertEquals(expectedEntity.getUserEmail(), resultEntity.getUserEmail());
        assertEquals(expectedEntity.getPw(), resultEntity.getPw());
    }

    @Test
    public void testUpdatePw() {
        IUserRepository repository = Factory.get(UserRepository.class);
        String newPw = Toolbox.getRandomString(9, true, true) + "?9Aa";

        LoginBean loginBean = new LoginBean();
        loginBean.setPw(newPw);
        getTarget().path(UserService.WEB_CONTEXT_PATH)
                .path("update_pw")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .put(Entity.json(loginBean), String.class);
        logApiRequestTime();
        Optional<User> optionalEntity = repository.get(1);

        if (optionalEntity.isPresent()) {
            String hashedPassword = repository.hashedPassword(1);
            assertTrue(BCrypt.checkpw(newPw, hashedPassword));
        } else {
            fail();
        }
    }

    @Test
    public void testNewPw() {
        final String pw = TestUtils.DEFAULT_PW;
        IUserRepository repository = Factory.get(UserRepository.class);
        assertTrue(BCrypt.checkpw(pw, repository.hashedPassword(1)));
        getTarget().path(UserService.WEB_CONTEXT_PATH)
                .path("new_pw")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();
        Optional<User> optionalEntity = repository.get(1);

        if (optionalEntity.isPresent()) {
            assertFalse(BCrypt.checkpw(pw, repository.hashedPassword(1)));
        } else {
            fail();
        }
    }

    @Test
    public void testAll() {
        String response = getTarget()
                .path(UserService.WEB_CONTEXT_PATH)
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();
        Type listType = new TypeToken<List<User>>() {
        }.getType();

        List<User> result = new Gson().fromJson(response, listType);

        assertTrue(result.stream().anyMatch(u -> "user@email1".equals(u.getUserEmail())));
    }

    @Test
    public void testSave() {
        User entity = new User();
        entity.setUserEmail("login1");
        entity.setPw(TestUtils.DEFAULT_PW);
        String result = getTarget()
                .path(UserService.WEB_CONTEXT_PATH)
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .post(Entity.json(entity), String.class);
        logApiRequestTime();

        assertEquals(TestUtils.OK_RESULT, result);
    }

    @Test
    public void testUpdate() {
        Optional<User> optionalEntity = Factory.get(UserRepository.class).get(1);

        if (optionalEntity.isPresent()) {
            User entity = optionalEntity.get();
            entity.setPw("pw");
            String result = getTarget()
                    .path(UserService.WEB_CONTEXT_PATH)
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
    public void testDelete() {
        String result = getTarget()
                .path(UserService.WEB_CONTEXT_PATH)
                .path("delete")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .delete(String.class);
        logApiRequestTime();

        assertEquals(TestUtils.OK_RESULT, result);
    }
}
