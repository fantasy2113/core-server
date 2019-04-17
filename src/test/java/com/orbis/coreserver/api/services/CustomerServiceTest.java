package com.orbis.coreserver.api.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.entities.Customer;
import com.orbis.coreserver.repositories.CustomerRepository;
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

public class CustomerServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
        TestUtils.setConfig();
        TestUtils.cleanTables();
        TestUtils.setServiceRights(1, CustomerService.WEB_CONTEXT_PATH);
    }

    @Before
    public void setUp() {
        TestUtils.cleanAndInsertCustomerDemoData();
        TestUtils.setServiceRights(1, CustomerService.WEB_CONTEXT_PATH);
    }

    @Test
    public void testGet() {
        Customer expectedEntity = new Customer();

        Optional<Customer> optionalEntity = Factory.get(CustomerRepository.class).get(1);

        if (!optionalEntity.isPresent()) {
            fail();
        } else {
            expectedEntity = optionalEntity.get();
        }

        String response = getTarget()
                .path(CustomerService.WEB_CONTEXT_PATH)
                .path("get")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();

        Customer resultEntity = new Gson().fromJson(response, Customer.class);

        assertEquals(expectedEntity.getId(), resultEntity.getId());
        assertEquals(expectedEntity.isCommercial(), resultEntity.isCommercial());
        assertEquals(expectedEntity.getCompanyName(), resultEntity.getCompanyName());
        assertTrue(expectedEntity.getAddress() != null);
        assertTrue(expectedEntity.getPerson() != null);
    }

    @Test
    public void testAll() {

        String response = getTarget()
                .path(CustomerService.WEB_CONTEXT_PATH)
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();

        Type listType = new TypeToken<List<Customer>>() {
        }.getType();

        List<Customer> result = new Gson().fromJson(response, listType);

        assertTrue(result.size() == 2);
    }

    @Test
    public void testSave() {
        Customer entity = new Customer();
        entity.setAddress(TestUtils.addressBean());
        entity.setPerson(TestUtils.personBean());
        entity.setCommercial(true);
        entity.setCompanyName("Company");

        String result = getTarget()
                .path(CustomerService.WEB_CONTEXT_PATH)
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .post(Entity.json(entity), String.class);
        logApiRequestTime();

        assertEquals(TestUtils.OK_RESULT, result);
    }

    @Test
    public void testUpdate() {
        Optional<Customer> optionalEntity = Factory.get(CustomerRepository.class).get(1);

        if (optionalEntity.isPresent()) {
            Customer entity = optionalEntity.get();

            String result = getTarget()
                    .path(CustomerService.WEB_CONTEXT_PATH)
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
                .path(CustomerService.WEB_CONTEXT_PATH)
                .path("delete")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .delete(String.class);
        logApiRequestTime();

        assertEquals(TestUtils.OK_RESULT, result);
    }

}
