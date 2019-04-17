package com.orbis.coreserver.api.services;

import com.orbis.coreserver.api.Service;
import com.orbis.coreserver.base.entities.Customer;
import com.orbis.coreserver.base.interfaces.repositories.ICustomerRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path(CustomerService.WEB_CONTEXT_PATH)
public class CustomerService extends Service<Customer> {

    public static final String WEB_CONTEXT_PATH = "customer";
    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class.getName()); //NOSONAR

    @Inject
    public CustomerService(ICustomerRepository customerRepository, IUserRightRepository apiRightRepository) {
        super(WEB_CONTEXT_PATH, apiRightRepository, customerRepository, LOGGER);
    }

}
