package de.josmer.coreserver.repositories;

import de.josmer.coreserver.base.entities.Customer;
import de.josmer.coreserver.base.interfaces.repositories.ICustomerRepository;
import org.junit.Assert;
import org.junit.Test;
import utils.Factory;
import utils.TestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class CustomerRepositoryTest extends RepositoryTesting {

    @Test
    public void testGet() {
        TestUtils.saveCustomer();
        Optional<Customer> entity = Factory.get(CustomerRepository.class).get(1);

        assertTrue(entity.isPresent());
    }

    @Test
    public void testGetAll() {
        ICustomerRepository instance = Factory.get(CustomerRepository.class);
        int expected = 2;

        TestUtils.insertCustomerDemoData();

        List<Customer> entities = instance.getAll();

        assertNotNull(entities);

        assertEquals(expected, entities.size());
    }

    @Test
    public void testSave() {
        ICustomerRepository instance = Factory.get(CustomerRepository.class);
        TestUtils.saveCustomer();
        Optional<Customer> entity = instance.get(1);

        if (entity.isPresent()) {
            assertEquals(1, entity.get().getId());
            assertEquals(false, entity.get().isCommercial());
            assertEquals("Hansen & Co KG", entity.get().getCompanyName());
            testPerson(entity.get().getPerson());
            testAddress(entity.get().getAddress());

        } else {
            fail();
        }
    }

    @Test
    public void testUpdate() {
        ICustomerRepository instance = Factory.get(CustomerRepository.class);
        TestUtils.saveCustomer();
        Optional<Customer> entity1 = instance.get(1);

        if (entity1.isPresent()) {
            Customer get = entity1.get();

            get.setCommercial(true);
            get.setCompanyName("Hansen & Co KG_");
            updatePerson(get.getPerson());
            updateAddress(get.getAddress());

            Assert.assertEquals(1, instance.update(get).getUpdates());
        } else {
            fail();
        }

        Optional<Customer> entity2 = instance.get(1);
        if (entity2.isPresent()) {
            assertEquals(1, entity2.get().getId());
            assertEquals(true, entity2.get().isCommercial());
            assertEquals("Hansen & Co KG_", entity2.get().getCompanyName());
            testPersonAfterUpdate(entity2.get().getPerson());
            testAddressAfterUpdate(entity2.get().getAddress());
        } else {
            fail();
        }
    }

    @Test
    public void testDelete() {
        TestUtils.saveCustomer();
        ICustomerRepository instance = Factory.get(CustomerRepository.class);

        Optional<Customer> entity1 = instance.get(1);
        if (entity1.isPresent()) {
            Assert.assertEquals(1, instance.delete(entity1.get().getId()).getUpdates());
        } else {
            fail();
        }

        Optional<Customer> entity2 = instance.get(1);

        if (entity2.isPresent()) {
            fail();
        }
    }

}
