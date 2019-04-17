package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.entities.CustomerContact;
import com.orbis.coreserver.base.interfaces.repositories.ICustomerContactRepository;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomerContactRepositoryTest extends RepositoryTesting {

    @Test
    public void testGet() {
        TestUtils.saveCustomerContact();
        Optional<CustomerContact> entity = Factory.get(CustomerContactRepository.class).get(1);

        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetAll() {
        ICustomerContactRepository instance = Factory.get(CustomerContactRepository.class);
        int expected = 2;

        TestUtils.insertCustomerContactDemoData();

        List<CustomerContact> entities = instance.getAll();

        assertNotNull(entities);

        assertEquals(expected, entities.size());
    }

    @Test
    public void testGetAllFromForeignKey() {
        ICustomerContactRepository instance = Factory.get(CustomerContactRepository.class);
        int expected = 2;

        TestUtils.insertCustomerContactDemoData();

        List<CustomerContact> entities = instance.getAll(1);

        assertNotNull(entities);

        assertEquals(expected, entities.size());

        entities = instance.getAll(0);

        assertEquals(0, entities.size());
    }

    @Test
    public void testSave() {
        ICustomerContactRepository instance = Factory.get(CustomerContactRepository.class);
        TestUtils.saveCustomerContact();
        Optional<CustomerContact> entity = instance.get(1);

        if (entity.isPresent()) {
            assertEquals(1, entity.get().getId());
            assertEquals(1, entity.get().getCustomerId());
            testPerson(entity.get().getPerson());
            testAddress(entity.get().getAddress());

        } else {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        ICustomerContactRepository instance = Factory.get(CustomerContactRepository.class);
        TestUtils.saveCustomerContact();
        Optional<CustomerContact> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            CustomerContact get = entity1.get();

            get.setCustomerId(2);
            updatePerson(get.getPerson());
            updateAddress(get.getAddress());

            assertEquals(1, instance.update(get).getUpdates());
        } else {
            fail();
        }

        Optional<CustomerContact> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertEquals(1, entity2.get().getId());
            assertEquals(2, entity2.get().getCustomerId());
            testPersonAfterUpdate(entity2.get().getPerson());
            testAddressAfterUpdate(entity2.get().getAddress());
        } else {
            fail();
        }
    }

    @Test
    public void testDelete() {
        TestUtils.saveCustomerContact();
        ICustomerContactRepository instance = Factory.get(CustomerContactRepository.class);

        Optional<CustomerContact> entity1 = instance.get(1);
        if (entity1.isPresent()) {
            assertEquals(1, instance.delete(entity1.get().getId()).getUpdates());
        } else {
            fail();
        }

        Optional<CustomerContact> entity2 = instance.get(1);

        if (entity2.isPresent()) {
            fail();
        }
    }

}
