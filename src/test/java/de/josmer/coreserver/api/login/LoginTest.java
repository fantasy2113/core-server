package de.josmer.coreserver.api.login;

import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.login.ILogin;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.Optional;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginTest {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.setConfig();
    }

    @Before
    public void setUp() {
        TestUtils.cleanTable(EntityTypes.USER);
    }

    @Test
    public void testIsAuthentication1() {
        IUserRepository userRep = Factory.get(UserRepository.class);
        ILogin login = Factory.get(Login.class);

        User user = new User();
        user.setUserEmail("login_1");
        user.setPw(TestUtils.DEFAULT_PW);
        userRep.save(user);

        assertEquals(true, login.userAuthentication(user.getUserEmail(), TestUtils.DEFAULT_PW).isPresent());
        assertEquals(false, login.userAuthentication(user.getUserEmail(), "pwd1").isPresent());
        assertEquals(1, login.userAuthenticationFails(user.getUserEmail()));
        assertEquals(true, login.userAuthentication(user.getUserEmail(), TestUtils.DEFAULT_PW).isPresent());
    }

    @Test
    public void testIsAuthentication2() {
        IUserRepository userRep = Factory.get(UserRepository.class);
        ILogin login = Factory.get(Login.class);

        User user = new User();
        user.setUserEmail("user_1");
        user.setPw(TestUtils.DEFAULT_PW);
        userRep.save(user);

        for (int i = 0; i < 8; i++) {
            Optional<User> optionalUser = userRep.get("user_1");
            if (optionalUser.isPresent()) {
                login.userAuthentication(user.getUserEmail(), TestUtils.DEFAULT_PW + 1);
            } else {
                fail();
            }
        }
        assertEquals(8, login.userAuthenticationFails(user.getUserEmail()));
        if (!userRep.get("user_1").isPresent()) {
            fail();
        }

        login.userAuthentication(user.getUserEmail(), TestUtils.DEFAULT_PW);
        assertEquals(0, login.userAuthenticationFails(user.getUserEmail()));
    }

    @Test
    public void testIsAuthentication3() {
        IUserRepository userRep = Factory.get(UserRepository.class);
        ILogin login = Factory.get(Login.class);

        User user = new User();
        user.setUserEmail("user_1");
        user.setPw(TestUtils.DEFAULT_PW);
        userRep.save(user);

        for (int i = 0; i < 11; i++) {
            Optional<User> optionalUser = userRep.get("user_1");
            if (optionalUser.isPresent()) {
                login.userAuthentication(user.getUserEmail(), TestUtils.DEFAULT_PW + 1);
            } else {
                fail();
            }
        }

        if (userRep.get("user_1").isPresent()) {
            fail();
        }
        assertEquals(10, login.userAuthenticationFails(user.getUserEmail()));
    }

    @Test
    public void testIsAuthentication4() {
        ILogin login = Factory.get(Login.class);
        assertEquals(false, login.userAuthentication(null, null).isPresent());
        assertEquals(false, login.userAuthentication("", null).isPresent());
        assertEquals(false, login.userAuthentication(null, "").isPresent());
        assertEquals(false, login.userAuthentication(" ", " ").isPresent());
    }

    @Test
    public void testIsAuthentication5() {
        assertFalse(Login.isPassword(null, null));
        assertFalse(Login.isPassword("", null));
        assertFalse(Login.isPassword(null, ""));

        assertFalse(Login.isPassword(" ", ""));
        assertFalse(Login.isPassword(" ", ""));
        assertFalse(Login.isPassword("", " "));
        assertFalse(Login.isPassword("", ""));
    }
}
