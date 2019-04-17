package com.orbis.coreserver.api.services;

import com.orbis.coreserver.api.Service;
import com.orbis.coreserver.base.entities.User;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.entities.beans.RightsBean;
import com.orbis.coreserver.base.enums.Rights;
import com.orbis.coreserver.base.interfaces.repositories.IUserRepository;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path(UserRightService.WEB_CONTEXT_PATH)
public final class UserRightService extends Service<UserRight> {

    public static final String WEB_CONTEXT_PATH = "user_right";
    private static final Logger LOGGER = LogManager.getLogger(UserRightService.class.getName());
    private final IUserRightRepository userRightRepository;
    private final IUserRepository userRepository;

    @Inject
    public UserRightService(IUserRightRepository userRightRepository, IUserRepository userRepository) {
        super(WEB_CONTEXT_PATH, userRightRepository, userRightRepository, LOGGER);
        this.userRightRepository = userRightRepository;
        this.userRepository = userRepository;
    }

    @GET
    @Path("getRights/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public final Response getRights(@HeaderParam("token") final String token, @PathParam("id") final int userId) {
        LOGGER.info("GET");
        try {
            if (isAccessDenied(token, Rights.GET)) {
                return responseAccessDenied();
            }
            Optional<User> optionalUser = userRepository.get(userId);
            if (optionalUser.isPresent()) {
                return Response.status(200).entity(userRightRepository.getRights(optionalUser.get())).build();
            }
            return responseNothingFound();
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @PUT
    @Path("updateRights")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateRights(@HeaderParam("token") final String token, final RightsBean rightsBean) {
        LOGGER.info("PUT");
        try {
            if (isAccessDenied(token, Rights.PUT)) {
                return responseAccessDenied();
            }
            return response(userRightRepository.updateRights(rightsBean));
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }
}
