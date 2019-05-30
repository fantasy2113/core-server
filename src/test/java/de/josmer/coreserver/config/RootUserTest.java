package de.josmer.coreserver.config;

import de.josmer.coreserver.api.services.UserRightService;
import de.josmer.coreserver.api.services.UserService;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.UserRight;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import de.josmer.coreserver.repositories.UserRepository;
import de.josmer.coreserver.repositories.UserRightRepository;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.List;

public class RootUserTest {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.setConfig();
        clean();
    }

    @AfterClass
    public static void tearDownClass() {
        clean();
    }

    private static void clean() {
        TestUtils.cleanTable(EntityTypes.USER_RIGHT);
        TestUtils.cleanTable(EntityTypes.USER);
    }

    @Test
    public void testCreate1() {
        RootUser rootUser = new RootUser(
                UserRightService.WEB_CONTEXT_PATH,
                UserService.WEB_CONTEXT_PATH
        );
        rootUser.create();

        IUserRepository userRepository = Factory.get(UserRepository.class);
        IUserRightRepository apiRightRepository = Factory.get(UserRightRepository.class);

        List<User> users = userRepository.getAll();
        List<UserRight> apiRights = apiRightRepository.getAll();

        Assert.assertTrue(!users.isEmpty() && users.size() == 1);
        Assert.assertTrue(!apiRights.isEmpty() && apiRights.size() > 1);
        Assert.assertTrue(users.stream().anyMatch(u -> u.getUserEmail().equals("root")));
    }

    @Test
    public void testCreate2() {
        IUserRepository userRepository = Factory.get(UserRepository.class);
        User user = new User();
        user.setUserEmail("root");
        user.setPw(TestUtils.DEFAULT_PW);

        RootUser rootUser = new RootUser(
                UserRightService.WEB_CONTEXT_PATH,
                UserService.WEB_CONTEXT_PATH
        );
        rootUser.create();

        IUserRightRepository apiRightRepository = Factory.get(UserRightRepository.class);
        TestUtils.setServiceRights(1, UserService.WEB_CONTEXT_PATH);

        List<User> users = userRepository.getAll();
        List<UserRight> apiRights = apiRightRepository.getAll();

        Assert.assertTrue(!users.isEmpty() && users.size() == 1);
        Assert.assertTrue(!apiRights.isEmpty() && apiRights.size() > 1);
        Assert.assertTrue(users.stream().anyMatch(u -> u.getUserEmail().equals("root")));
    }

}
