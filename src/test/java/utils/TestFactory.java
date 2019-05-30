package utils;

import de.josmer.coreserver.api.login.Login;
import de.josmer.coreserver.connector.MySqlConnector;
import de.josmer.coreserver.connector.UserConnector;
import de.josmer.coreserver.connector.mapper.Mapper;
import de.josmer.coreserver.repositories.*;

public final class TestFactory {

    private TestFactory() {
    }

    public static void init() {
        Factory.add(UserRightRepository.class.getTypeName(), new UserRightRepository(new MySqlConnector(new Mapper())));
        Factory.add(CustomerAddressRepository.class.getTypeName(), new CustomerAddressRepository(new MySqlConnector(new Mapper())));
        Factory.add(CustomerContactRepository.class.getTypeName(), new CustomerContactRepository(new MySqlConnector(new Mapper())));
        Factory.add(CustomerRepository.class.getTypeName(), new CustomerRepository(new MySqlConnector(new Mapper())));
        Factory.add(LogRepository.class.getTypeName(), new LogRepository());
        Factory.add(UserRepository.class.getTypeName(), new UserRepository(new MySqlConnector(new Mapper()), new UserConnector()));
        Factory.add(MySqlConnector.class.getTypeName(), new MySqlConnector(new Mapper()));
        Factory.add(Login.class.getTypeName(), new Login(new UserRepository(new MySqlConnector(new Mapper()), new UserConnector())));
        Factory.add(UserConnector.class.getTypeName(), new UserConnector());
    }
}
