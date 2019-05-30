package de.josmer.coreserver.api.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.base.entities.CustomerAddress;
import de.josmer.coreserver.repositories.CustomerAddressRepository;
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

public class CustomerAddressServiceTest extends ServiceTesting {

    @BeforeClass
    public static void setUpClass() {
        TestUtils.startServer();
        TestUtils.setConfig();
        TestUtils.cleanTables();
        TestUtils.setServiceRights(1, CustomerAddressService.WEB_CONTEXT_PATH);
    }

    @Before
    public void setUp() {
        TestUtils.cleanAndInsertCustomerAddressDemoData();
        TestUtils.setServiceRights(1, CustomerAddressService.WEB_CONTEXT_PATH);
    }

    @Test
    public void testGet() {
        CustomerAddress expectedEntity = new CustomerAddress();

        Optional<CustomerAddress> optionalEntity = Factory.get(CustomerAddressRepository.class).get(1);

        if (!optionalEntity.isPresent()) {
            fail();
        } else {
            expectedEntity = optionalEntity.get();
        }

        String response = getTarget()
                .path(CustomerAddressService.WEB_CONTEXT_PATH)
                .path("get")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();

        CustomerAddress resultEntity = new Gson().fromJson(response, CustomerAddress.class);

        assertEquals(expectedEntity.getId(), resultEntity.getId());
        assertEquals(expectedEntity.getType(), resultEntity.getType());
        assertEquals(expectedEntity.getCustomerId(), resultEntity.getCustomerId());
        assertTrue(expectedEntity.getAddress() != null);
    }

    @Test
    public void testAll() {

        String response = getTarget()
                .path(CustomerAddressService.WEB_CONTEXT_PATH)
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);
        logApiRequestTime();

        Type listType = new TypeToken<List<CustomerAddress>>() {
        }.getType();

        List<CustomerAddress> result = new Gson().fromJson(response, listType);

        assertTrue(result.size() == 2);
    }

    @Test
    public void testAllForeignKey() {

        String response = getTarget()
                .path(CustomerAddressService.WEB_CONTEXT_PATH)
                .path("all")
                .path("key")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .get(String.class);

        logApiRequestTime();

        Type listType = new TypeToken<List<CustomerAddress>>() {
        }.getType();

        List<CustomerAddress> result = new Gson().fromJson(response, listType);

        assertTrue(result.size() == 2);
    }

    @Test
    public void testSave() {
        CustomerAddress entity = new CustomerAddress();
        entity.setAddress(TestUtils.addressBean());
        entity.setType("Lieferanschrift");
        entity.setCustomerId(2);

        String result = getTarget()
                .path(CustomerAddressService.WEB_CONTEXT_PATH)
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .post(Entity.json(entity), String.class);
        logApiRequestTime();
        assertEquals(TestUtils.OK_RESULT, result);
    }

    @Test
    public void testUpdate() {
        Optional<CustomerAddress> optionalEntity = Factory.get(CustomerAddressRepository.class).get(1);

        if (optionalEntity.isPresent()) {
            CustomerAddress entity = optionalEntity.get();
            String result = getTarget()
                    .path(CustomerAddressService.WEB_CONTEXT_PATH)
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
                .path(CustomerAddressService.WEB_CONTEXT_PATH)
                .path("delete")
                .path("1")
                .request(MediaType.APPLICATION_JSON)
                .header("token", Token.get(1))
                .delete(String.class);
        logApiRequestTime();
        assertEquals(TestUtils.OK_RESULT, result);
    }

}
