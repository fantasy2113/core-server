package com.orbis.coreserver.api.services;

import com.orbis.coreserver.api.Service;
import com.orbis.coreserver.base.entities.CustomerAddress;
import com.orbis.coreserver.base.interfaces.repositories.ICustomerAddressRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(CustomerAddressService.WEB_CONTEXT_PATH)
public class CustomerAddressService extends Service<CustomerAddress> {

    public static final String WEB_CONTEXT_PATH = "customer_address";
    private static final Logger LOGGER = LogManager.getLogger(CustomerAddressService.class.getName());
    private final ICustomerAddressRepository customerAddressRepository;

    @Inject
    public CustomerAddressService(ICustomerAddressRepository customerAddressRepository, IUserRightRepository apiRightRepository) {
        super(WEB_CONTEXT_PATH, apiRightRepository, customerAddressRepository, LOGGER);
        this.customerAddressRepository = customerAddressRepository;
    }

    @GET
    @Path(PATH_ALL_KEY)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll(@HeaderParam("token") final String token, @PathParam("key") final int foreignKey) {
        LOGGER.info("get");
        try {
            return getEntitiesFromForeignKey(token, foreignKey, customerAddressRepository);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }
}
