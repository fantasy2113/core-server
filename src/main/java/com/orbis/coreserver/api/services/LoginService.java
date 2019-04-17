package com.orbis.coreserver.api.services;

import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.entities.beans.ResponseBean;
import com.orbis.coreserver.base.entities.beans.TokenBean;
import com.orbis.coreserver.base.interfaces.login.ILogin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.OptionalInt;

@Path(LoginService.WEB_CONTEXT_PATH)
public final class LoginService {
    public static final String PATH_AUTHENTICATION = "authentication";
    public static final String WEB_CONTEXT_PATH = "login";
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class.getName());
    private final ILogin login;

    @Inject
    public LoginService(ILogin login) {
        this.login = login;
    }

    @GET
    @Path(PATH_AUTHENTICATION)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public final Response authentication(@HeaderParam("login") final String userLogin, @HeaderParam("pw") final String pw) {
        LOGGER.info("GET");
        try {
            final OptionalInt optionalUserId = login.userAuthentication(userLogin, pw);
            if (optionalUserId.isPresent()) {
                TokenBean tokenResponse = new TokenBean();
                tokenResponse.setToken(Token.get(optionalUserId.getAsInt()));
                return Response.status(200).entity(tokenResponse).build();
            }
            ResponseBean failResponse = new ResponseBean();
            failResponse.setMessage("login failed");
            return Response.status(200).entity(failResponse).build();
        } catch (Exception ex) {
            LOGGER.info(ex.getMessage());
            ResponseBean errorResponse = new ResponseBean();
            errorResponse.setMessage(ex.getMessage());
            return Response.status(200).entity(errorResponse).build();
        }
    }
}
