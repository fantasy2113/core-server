package de.josmer.coreserver.api.services;

import de.josmer.coreserver.api.Service;
import de.josmer.coreserver.base.entities.CustomerContact;
import de.josmer.coreserver.base.interfaces.repositories.ICustomerContactRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(CustomerContactService.WEB_CONTEXT_PATH)
public class CustomerContactService extends Service<CustomerContact> {

    public static final String WEB_CONTEXT_PATH = "customer_contact";
    private static final Logger LOGGER = LogManager.getLogger(CustomerContactService.class.getName());
    private final ICustomerContactRepository customerContactRepository;

    @Inject
    public CustomerContactService(ICustomerContactRepository customerContactRepository, IUserRightRepository apiRightRepository) {
        super(WEB_CONTEXT_PATH, apiRightRepository, customerContactRepository, LOGGER);
        this.customerContactRepository = customerContactRepository;
    }

    @GET
    @Path(PATH_ALL_KEY)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll(@HeaderParam("token") final String token, @PathParam("key") final int foreignKey) {
        LOGGER.info("get");
        try {
            return getEntitiesFromForeignKey(token, foreignKey, customerContactRepository);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

}
