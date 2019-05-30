package de.josmer.coreserver.api.services;

import de.josmer.coreserver.api.Service;
import de.josmer.coreserver.api.security.token.Token;
import de.josmer.coreserver.base.Authentication;
import de.josmer.coreserver.base.Toolbox;
import de.josmer.coreserver.base.entities.User;
import de.josmer.coreserver.base.entities.beans.LoginBean;
import de.josmer.coreserver.base.interfaces.repositories.IUserRepository;
import de.josmer.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(UserService.WEB_CONTEXT_PATH)
public final class UserService extends Service<User> {

    public static final String WEB_CONTEXT_PATH = "user";
    private static final Logger LOGGER = LogManager.getLogger(UserService.class.getName());
    private final IUserRepository userRepository;

    @Inject
    public UserService(IUserRepository userRepository, IUserRightRepository apiRightRepository) {
        super(WEB_CONTEXT_PATH, apiRightRepository, userRepository, LOGGER);
        this.userRepository = userRepository;
    }

    @PUT
    @Path("/update_pw")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public final Response updatePassword(@HeaderParam("token") final String token, final LoginBean loginBean) {
        LOGGER.info("PUT");
        try {
            final Authentication authentication = Token.getAuthentication(token);
            if (isAccessDenied(authentication)) {
                return responseAccessDenied();
            }
            //noinspection OptionalGetWithoutIsPresent
            return response(userRepository.updatePassword(authentication.getUserId().getAsInt(), loginBean.getPw()).getUpdates());
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @GET
    @Path("/new_pw")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response newPassword(@HeaderParam("token") String token) {
        LOGGER.info("GET");
        try {
            final Authentication authentication = Token.getAuthentication(token);
            if (isAccessDenied(authentication)) {
                return responseAccessDenied();
            }
            final String newPassword = Toolbox.getRandomString(9, true, true) + "1!Os";
            //noinspection OptionalGetWithoutIsPresent
            if (userRepository.newPassword(authentication.getUserId().getAsInt(), newPassword).getUpdates() == 1) {
                return response(newPassword);
            }
            return response("something went wrong");
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }
}
