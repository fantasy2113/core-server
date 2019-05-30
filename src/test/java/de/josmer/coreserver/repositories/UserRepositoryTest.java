package de.josmer.coreserver.repositories;

import de.josmer.coreserver.api.login.Login;
import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.repositories.IRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import utils.Factory;
import utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserRepositoryTest extends RepositoryTesting {

    private static final String USER_EMAIL = "login";

    @Test
    public void testGetTableName() {
        IRepository<User> instance = Factory.get(UserRepository.class);
        Assert.assertEquals(EntityTypes.USER.toString(), instance.getTableName());
    }

    @Test
    public void testGet() {
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity = Factory.get(UserRepository.class).get(1);

        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetFromLogin() {
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            Optional<User> entity2 = instance.get(entity1.get().getUserEmail());

            assertTrue(entity2.isPresent());
        } else {
            fail();
        }
    }

    @Test
    public void testGetPw() {
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            String hashedPassword = instance.hashedPassword(1);
            assertTrue(BCrypt.checkpw(TestUtils.DEFAULT_PW, hashedPassword));
        } else {
            fail();
        }
    }

    @Test
    public void testUpdatePw() {
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            final String newPw = Toolbox.getRandomString(9, true, true) + "?9Aa";
            Assert.assertEquals(1, instance.updatePassword(entity1.get().getId(), newPw).getUpdates());
            String hashedPassword = instance.hashedPassword(1);
            assertTrue(BCrypt.checkpw(newPw, hashedPassword));
        } else {
            fail();
        }
    }

    @Test
    public void testNewPw() {
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            final String newPw = Toolbox.getRandomString(9, true, true) + "?9Aa";
            Assert.assertEquals(1, instance.newPassword(entity1.get().getId(), newPw).getUpdates());
            String hashedPassword = instance.hashedPassword(1);
            assertTrue(BCrypt.checkpw(newPw, hashedPassword));
        } else {
            fail();
        }
    }

    @Test
    public void testGetAll() {
        IUserRepository instance = Factory.get(UserRepository.class);
        int expected = 2;

        TestUtils.insertDemoUserData();

        List<User> entities = instance.getAll();

        assertNotNull(entities);

        assertEquals(expected, entities.size());
    }

    @Test
    public void testSave1() {
        IUserRepository instance = Factory.get(UserRepository.class);
        User user = new User();
        user.setUserEmail("");

        user.setUserEmail(null);
        user.setPw(null);
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail(" ");
        user.setPw(null);
        Assert.assertEquals(0, instance.save(user).getUpdates());


        user.setUserEmail(null);
        user.setPw(" ");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("");
        user.setPw("");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail(" ");
        user.setPw("");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("");
        user.setPw(" ");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("1");
        user.setPw("p");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("asdfghjkl");
        user.setPw("_asdfghjkl_");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("login");
        user.setPw("asdfghjkl");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("login");
        user.setPw("ASDFGHJKL");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("login");
        user.setPw("ASDfGHjKL");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("login");
        user.setPw("ASDfGHjKL9");
        Assert.assertEquals(0, instance.save(user).getUpdates());

        user.setUserEmail("login");
        user.setPw("ASDfGHjKL9!");
        Assert.assertEquals(1, instance.save(user).getUpdates());
    }

    @Test
    public void testSave2() {
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity = instance.get(1);
        if (entity.isPresent()) {
            assertEquals(USER_EMAIL, entity.get().getUserEmail());
            assertEquals("", entity.get().getPw());
            assertEquals(true, entity.get().isActive());
        } else {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        final String newEmail = "super@email";
        final String dateTimeNow = Toolbox.dateTimeNow();
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            entity1.get().setUserEmail(newEmail);
            entity1.get().setIsActive(false);
            entity1.get().setLastLogin(dateTimeNow);
            Assert.assertEquals(1, instance.update(entity1.get()).getUpdates());
        } else {
            fail();
        }

        Optional<User> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertEquals(newEmail, entity2.get().getUserEmail());
            assertEquals(false, entity2.get().isActive());
            assertTrue(entity2.get().getLastLogin().contains("2000-01-01"));
        } else {
            fail();
        }
    }

    @Test
    public void testUpdateLastLogin() {
        final String dateTimeNow = Toolbox.dateTimeNow();
        IUserRepository instance = Factory.get(UserRepository.class);
        TestUtils.saveUser(USER_EMAIL);
        Optional<User> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            entity1.get().setLastLogin(dateTimeNow);
            Assert.assertEquals(1, instance.updateLastLogin(entity1.get().getId(), dateTimeNow).getUpdates());
        } else {
            fail();
        }

        Optional<User> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertFalse(entity2.get().getLastLogin().contains("2000-01-01"));
        } else {
            fail();
        }
    }

    @Test
    public void testDelete() {
        TestUtils.saveUser(USER_EMAIL);
        IUserRepository instance = Factory.get(UserRepository.class);

        Optional<User> entity1 = instance.get(1);
        if (entity1.isPresent()) {
            Assert.assertEquals(1, instance.delete(entity1.get().getId()).getUpdates());
        } else {
            fail();
        }

        Optional<User> entity2 = instance.get(1);

        entity2.ifPresent(user -> assertEquals(0, user.getId()));
    }

    @Test
    public void testHashPasswordDefault() {
        String hashedPw = UserRepository.hashPassword(null);
        assertTrue(Login.isPassword("Orbis123!_", hashedPw));
    }
}
