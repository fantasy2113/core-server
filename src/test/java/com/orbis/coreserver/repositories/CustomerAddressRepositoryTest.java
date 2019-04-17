package com.orbis.coreserver.repositories;

import com.orbis.coreserver.base.entities.CustomerAddress;
import com.orbis.coreserver.base.interfaces.repositories.ICustomerAddressRepository;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomerAddressRepositoryTest extends RepositoryTesting {

    @Test
    public void testGet() {
        TestUtils.saveAddress();
        Optional<CustomerAddress> entity = Factory.get(CustomerAddressRepository.class).get(1);

        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetAll() {
        ICustomerAddressRepository instance = Factory.get(CustomerAddressRepository.class);
        int expected = 2;

        TestUtils.insertCustomerAddressDemoData();

        List<CustomerAddress> entities = instance.getAll();

        assertNotNull(entities);

        assertEquals(expected, entities.size());
    }

    @Test
    public void testGetAllFromForeignKey() {
        ICustomerAddressRepository instance = Factory.get(CustomerAddressRepository.class);
        int expected = 2;

        TestUtils.insertCustomerAddressDemoData();

        List<CustomerAddress> entities = instance.getAll(1);

        assertNotNull(entities);

        assertEquals(expected, entities.size());

        entities = instance.getAll(0);

        assertEquals(0, entities.size());
    }

    @Test
    public void testSave() {
        ICustomerAddressRepository instance = Factory.get(CustomerAddressRepository.class);
        TestUtils.saveAddress();
        Optional<CustomerAddress> entity = instance.get(1);

        if (entity.isPresent()) {
            assertEquals(1, entity.get().getId());
            assertEquals(1, entity.get().getCustomerId());
            assertEquals("Lieferanschrift", entity.get().getType());
            testAddress(entity.get().getAddress());
        } else {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        ICustomerAddressRepository instance = Factory.get(CustomerAddressRepository.class);
        TestUtils.saveAddress();
        Optional<CustomerAddress> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            CustomerAddress get = entity1.get();
            get.setType("Lieferanschrift_");
            get.setCustomerId(2);
            updateAddress(get.getAddress());
            assertEquals(1, instance.update(get).getUpdates());
        } else {
            fail();
        }

        Optional<CustomerAddress> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertEquals(1, entity2.get().getId());
            assertEquals(2, entity2.get().getCustomerId());
            assertEquals("Lieferanschrift_", entity2.get().getType());
            testAddressAfterUpdate(entity2.get().getAddress());
        } else {
            fail();
        }
    }

    @Test
    public void testDelete() {
        TestUtils.saveAddress();
        ICustomerAddressRepository instance = Factory.get(CustomerAddressRepository.class);

        Optional<CustomerAddress> entity1 = instance.get(1);
        if (entity1.isPresent()) {
            assertEquals(1, instance.delete(entity1.get().getId()).getUpdates());
        } else {
            fail();
        }

        Optional<CustomerAddress> entity2 = instance.get(1);

        if (entity2.isPresent()) {
            fail();
        }
    }

}
