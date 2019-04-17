package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.entities.beans.RightBean;
import com.orbis.coreserver.base.entities.beans.RightsBean;
import com.orbis.coreserver.base.enums.EntityTypes;
import com.orbis.coreserver.base.enums.Rights;
import com.orbis.coreserver.base.interfaces.repositories.IRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserRightRepositoryTest extends RepositoryTesting {

    @Test
    public void testGetTableName() {
        IRepository<UserRight> instance = Factory.get(UserRightRepository.class);
        assertEquals(EntityTypes.USER_RIGHT.toString(), instance.getTableName());
    }

    @Test
    public void testGet() {
        TestUtils.saveServiceRight();
        Optional<UserRight> entity = Factory.get(UserRightRepository.class).get(1);

        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetRights() {
        TestUtils.saveUser("user@abc.de");
        TestUtils.insertServiceRightDemoData();
        UserRightRepository userRightRepository = Factory.get(UserRightRepository.class);
        UserRepository userRepository = Factory.get(UserRepository.class);
        Optional<User> optionalUser = userRepository.get("user@abc.de");

        if (optionalUser.isPresent()) {
            RightsBean rightsBean = userRightRepository.getRights(optionalUser.get());
            assertEquals(optionalUser.get().getId(), rightsBean.getUserId());
            assertEquals(optionalUser.get().getUserEmail(), rightsBean.getLogin());
            assertTrue(rightsBean.getRights().size() == 2);
        } else {
            fail();
        }
    }

    @Test
    public void testUpdateRights() {
        TestUtils.saveUser("user@abc.de");
        TestUtils.insertServiceRightDemoData();
        UserRightRepository userRightRepository = Factory.get(UserRightRepository.class);
        UserRepository userRepository = Factory.get(UserRepository.class);
        Optional<User> optionalUser = userRepository.get("user@abc.de");

        if (optionalUser.isPresent()) {
            RightsBean rightsBean = userRightRepository.getRights(optionalUser.get());

            assertTrue(rightsBean.getRights().size() == 2);

            List<RightBean> rights = rightsBean.getRights();
            RightBean rightBean = new RightBean();
            rightBean.setServiceRight("DELETE");
            rightBean.setService("service");
            rights.add(rightBean);
            rightsBean.setRights(rights);

            assertEquals(1, userRightRepository.updateRights(rightsBean));

            rightsBean = userRightRepository.getRights(optionalUser.get());

            assertTrue(rightsBean.getRights().size() == 3);
        } else {
            fail();
        }
    }

    @Test
    public void testGetFromRightCheck() {
        String service = "service1";
        IUserRightRepository instance = Factory.get(UserRightRepository.class);
        TestUtils.setServiceRights(1, service);

        Optional<UserRight> entity = instance.get(1, service, Rights.PUT);
        assertTrue(entity.isPresent());

        entity = instance.get(1, service, Rights.GET);
        assertTrue(entity.isPresent());

        entity = instance.get(1, service, Rights.DELETE);
        assertTrue(entity.isPresent());

        entity = instance.get(1, service, Rights.POST);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetAll() {
        IUserRightRepository instance = Factory.get(UserRightRepository.class);
        int expected = 2;

        TestUtils.insertServiceRightDemoData();

        List<UserRight> entities = instance.getAll();

        assertNotNull(entities);

        assertEquals(expected, entities.size());
    }

    @Test
    public void testSave() {
        IUserRightRepository instance = Factory.get(UserRightRepository.class);
        TestUtils.saveServiceRight();
        Optional<UserRight> entity = instance.get(1);

        if (entity.isPresent()) {
            assertEquals(1, entity.get().getId());
            assertEquals("right1", entity.get().getServiceRight());
        } else {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        IUserRightRepository instance = Factory.get(UserRightRepository.class);
        TestUtils.saveServiceRight();
        Optional<UserRight> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            assertTrue(true);
            entity1.get().setServiceRight("right2");
            assertEquals(1, instance.update(entity1.get()).getUpdates());
        } else {
            fail();
        }

        Optional<UserRight> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertEquals("right2", entity2.get().getServiceRight());
        } else {
            fail();
        }
    }

    @Test
    public void testDelete() {
        TestUtils.saveServiceRight();
        IUserRightRepository instance = Factory.get(UserRightRepository.class);

        Optional<UserRight> entity1 = instance.get(1);
        if (entity1.isPresent()) {
            assertEquals(1, instance.delete(entity1.get().getId()).getUpdates());
        } else {
            fail();
        }

        Optional<UserRight> entity2 = instance.get(1);

        entity2.ifPresent(service -> assertEquals(-1, service.getId()));
    }

}
