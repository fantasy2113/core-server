package de.josmer.coreserver.api.services;

import com.google.gson.Gson;
import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.beans.TokenBean;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class LoginServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
    }

    @Before
    public void setUp() {
        TestUtils.setConfig();
        TestUtils.cleanTables();
    }

    @Test
    public void testAuthentication1() {
        IUserRepository repository = Factory.get(UserRepository.class);

        User user = new User();
        user.setUserEmail("login");
        user.setPw(TestUtils.DEFAULT_PW);

        repository.save(user);

        String response = getTarget()
                .path(LoginService.WEB_CONTEXT_PATH)
                .path(LoginService.PATH_AUTHENTICATION)
                .request(MediaType.APPLICATION_JSON)
                .header("login", "login")
                .header("pw", TestUtils.DEFAULT_PW)
                .get(String.class);
        logApiRequestTime();
        TokenBean tokenBean = new Gson().fromJson(response, TokenBean.class);

        Assert.assertEquals(tokenBean.getToken(), Token.get(1));
    }

    @Test
    public void testAuthentication2() {
        String response = getTarget()
                .path(LoginService.WEB_CONTEXT_PATH)
                .path("authentication")
                .request(MediaType.APPLICATION_JSON)
                .header("login", "hgfhgf")
                .header("pw", "hgjkhjlhl")
                .get(String.class);
        logApiRequestTime();

        assertEquals("{\"message\":\"login failed\"}", response);
    }

}
