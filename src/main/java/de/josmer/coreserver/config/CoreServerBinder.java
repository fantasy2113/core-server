package de.josmer.coreserver.config;

import de.josmer.coreserver.api.login.Login;
import de.josmer.coreserver.base.interfaces.connector.IConnector;
import de.josmer.coreserver.base.interfaces.connector.IMapper;
import de.josmer.coreserver.base.interfaces.connector.IUserConnector;
import de.josmer.coreserver.base.interfaces.login.ILogin;
import com.orbis.coreserver.base.interfaces.repositories.*;
import de.josmer.coreserver.connector.MySqlConnector;
import de.josmer.coreserver.connector.UserConnector;
import de.josmer.coreserver.connector.mapper.Mapper;
import com.orbis.coreserver.repositories.*;
import de.josmer.coreserver.base.interfaces.repositories.*;
import de.josmer.coreserver.repositories.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

final class CoreServerBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Mapper.class).named(Mapper.class.getSimpleName()).to(IMapper.class).in(Singleton.class);
        bind(MySqlConnector.class).named(MySqlConnector.class.getSimpleName()).to(IConnector.class).in(Singleton.class);
        bind(UserConnector.class).named(UserConnector.class.getSimpleName()).to(IUserConnector.class).in(Singleton.class);

        bind(Login.class).named(Login.class.getSimpleName()).to(ILogin.class).in(Singleton.class);

        bind(UserRepository.class).named(UserRepository.class.getSimpleName()).to(IUserRepository.class).in(Singleton.class);
        bind(UserRightRepository.class).named(UserRightRepository.class.getSimpleName()).to(IUserRightRepository.class).in(Singleton.class);
        bind(LogRepository.class).named(LogRepository.class.getSimpleName()).to(ILogRepository.class).in(Singleton.class);
        bind(CustomerRepository.class).named(CustomerRepository.class.getSimpleName()).to(ICustomerRepository.class).in(Singleton.class);
        bind(CustomerContactRepository.class).named(CustomerContactRepository.class.getSimpleName()).to(ICustomerContactRepository.class).in(Singleton.class);
        bind(CustomerAddressRepository.class).named(CustomerAddressRepository.class.getSimpleName()).to(ICustomerAddressRepository.class).in(Singleton.class);
    }

}
