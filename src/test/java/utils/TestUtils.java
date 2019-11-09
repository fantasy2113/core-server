package utils;

import de.josmer.coreserver.Run;
import de.josmer.coreserver.api.services.UserServiceTest;
import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.*;
import de.josmer.coreserver.base.entities.beans.AddressBean;
import de.josmer.coreserver.base.entities.beans.PersonBean;
import de.josmer.coreserver.base.enums.EntityTypes;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import de.josmer.coreserver.connector.MySqlConnector;
import de.josmer.coreserver.repositories.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TestUtils {

    public static final String SERVER_URL = "http://localhost:4434/api";
    public static final String OK_RESULT = "{\"message\":\"1\"}";
    public static final String PATH_TOKEN = "token";
    public static final String USER_ID = "user_id";
    public static final String DEFAULT_PW = "OrsWin9IstGeil!";
    private static final String ROLE_1 = "right1";
    private static final String ROLE = "right";

    private TestUtils() {
    }

    private static void executeQuery(String query) {
        try (Connection conn = DriverManager.getConnection(MySqlConnector.getUrl(), MySqlConnector.getUser(), MySqlConnector.getPw());
             Statement st = conn.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(TestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cleanTable(EntityTypes entity) {
        executeQuery("TRUNCATE TABLE " + entity.toString() + ";");
    }

    public static void cleanTables() {
        for (EntityTypes entity : EntityTypes.values()) {
            executeQuery("TRUNCATE TABLE " + entity.toString() + ";");
        }
    }

    public static void setConfig() {
        TestFactory.init();
        String[] args = new String[5];
        args[0] = "sql_user:=julian";
        args[1] = "sql_pwd:=qw999";
        args[2] = "sql_url:=jdbc:mysql://localhost:3306/coreserver_testing";
        args[3] = "server_url:=" + SERVER_URL;
        args[4] = "development";
        Run.configure(args);
    }

    public static void startServer() {
        setConfig();
        try {
            Run.start(true);
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(UserServiceTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    public static WebTarget getClient() {
        Client client = ClientBuilder.newClient();
        return client.target(TestUtils.SERVER_URL);
    }

    public static void setServiceRights(int userId, String service) {
        IUserRightRepository serviceRepository = Factory.get(UserRightRepository.class);
        UserRight entity = new UserRight();

        entity.setService(service);
        entity.setUserId(userId);

        for (String right : Toolbox.rights()) {
            entity.setServiceRight(right);
            serviceRepository.save(entity);
        }
    }

    public static void cleanAndInsertUserDemoData() {
        TestUtils.cleanTable(EntityTypes.USER);
        insertDemoUserData();
    }

    public static void insertDemoUserData() {
        IUserRepository repository = Factory.get(UserRepository.class);
        for (int nr = 1; nr < 3; nr++) {
            User entity = new User();
            entity.setUserEmail("user@email" + nr);
            entity.setPw(DEFAULT_PW);
            repository.save(entity);
        }
    }

    public static void saveUser(String login) {
        User entity = new User();
        entity.setUserEmail(login);
        entity.setPw(DEFAULT_PW);
        Factory.get(UserRepository.class).save(entity);
    }

    public static void cleanAndInsertUserRightDemoData() {
        TestUtils.cleanTable(EntityTypes.USER_RIGHT);
        insertServiceRightDemoData();
    }

    public static void insertServiceRightDemoData() {
        IUserRightRepository repository = Factory.get(UserRightRepository.class);

        UserRight entity = new UserRight();
        entity.setServiceRight("GET");
        entity.setService("service");
        entity.setUserId(1);
        repository.save(entity);

        entity = new UserRight();
        entity.setServiceRight("PUT");
        entity.setService("service");
        entity.setUserId(1);
        repository.save(entity);

    }

    public static void saveServiceRight() {
        UserRight entity = new UserRight();
        entity.setServiceRight(ROLE_1);
        entity.setService("service1");
        entity.setUserId(1);
        Factory.get(UserRightRepository.class).save(entity);
    }

    public static void cleanAndInsertCustomerAddressDemoData() {
        TestUtils.cleanTable(EntityTypes.CUSTOMER_ADDRESS);
        insertCustomerAddressDemoData();
    }

    public static void insertCustomerAddressDemoData() {
        for (int nr = 1; nr < 3; nr++) {
            saveAddress();
        }
    }

    public static void saveAddress() {
        CustomerAddress entity = new CustomerAddress();
        entity.setType("Lieferanschrift");
        entity.setCustomerId(1);
        entity.setAddress(addressBean());
        Factory.get(CustomerAddressRepository.class).save(entity);
    }

    public static AddressBean addressBean() {
        AddressBean bean = new AddressBean();
        bean.setStreetName("Super Str.");
        bean.setStreetNr("1");
        bean.setZipCode("20000");
        bean.setPlace("Oyten");
        bean.setTelephone("05-345-34");
        bean.setEmail("info@mail.de");
        bean.setFax("4324-5345-435345");
        return bean;
    }

    public static PersonBean personBean() {
        PersonBean bean = new PersonBean();
        bean.setTitle("Dr. med.");
        bean.setSalutation("Herr");
        bean.setFirstName("Karsten");
        bean.setLastName("Müller");
        return bean;
    }

    public static void cleanAndInsertCustomerContactDemoData() {
        TestUtils.cleanTable(EntityTypes.CUSTOMER_CONTACT);
        insertCustomerContactDemoData();
    }

    public static void saveCustomerContact() {
        CustomerContact entity = new CustomerContact();
        entity.setCustomerId(1);
        entity.setPerson(personBean());
        entity.setAddress(addressBean());
        Factory.get(CustomerContactRepository.class).save(entity);
    }

    public static void insertCustomerContactDemoData() {
        for (int nr = 1; nr < 3; nr++) {
            saveCustomerContact();
        }
    }

    public static void cleanAndInsertCustomerDemoData() {
        TestUtils.cleanTable(EntityTypes.CUSTOMER);
        insertCustomerDemoData();
    }

    public static void saveCustomer() {
        Customer entity = new Customer();
        entity.setPerson(personBean());
        entity.setAddress(addressBean());
        entity.setCommercial(false);
        entity.setCompanyName("Hansen & Co KG");
        Factory.get(CustomerRepository.class).save(entity);
    }

    public static void insertCustomerDemoData() {
        for (int nr = 1; nr < 3; nr++) {
            saveCustomer();
        }
    }
}
