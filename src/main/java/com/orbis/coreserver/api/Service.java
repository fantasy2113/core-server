package com.orbis.coreserver.api;

import com.orbis.coreserver.api.security.token.Token;
import com.orbis.coreserver.base.Authentication;
import com.orbis.coreserver.base.entities.AbstractEntity;
import com.orbis.coreserver.base.entities.UserRight;
import com.orbis.coreserver.base.entities.beans.ResponseBean;
import com.orbis.coreserver.base.enums.Rights;
import com.orbis.coreserver.base.interfaces.repositories.IRepository;
import com.orbis.coreserver.base.interfaces.repositories.IRepositoryFromForeignKey;
import com.orbis.coreserver.base.interfaces.repositories.IUserRightRepository;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

public abstract class Service<E extends AbstractEntity> {
    protected static final String PATH_GET = "get/{id}";
    protected static final String PATH_ALL = "all";
    protected static final String PATH_ALL_KEY = PATH_ALL + "/key/{key}";
    protected static final String PATH_DELETE = "delete/{id}";

    private final Logger logger;
    private final IUserRightRepository userRightRepository;
    private final String webContextPath;
    private final IRepository<E> repository;

    protected Service(final String webContextPath, final IUserRightRepository userRightRepository, final IRepository<E> repository, final Logger logger) {
        this.webContextPath = webContextPath;
        this.userRightRepository = userRightRepository;
        this.repository = repository;
        this.logger = logger;
    }

    @GET
    @Path(PATH_GET)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response get(@HeaderParam("token") final String token, @PathParam("id") final int getId) {
        logger.info("GET");
        try {
            return getEntity(token, getId);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @GET
    @Path(PATH_ALL)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAll(@HeaderParam("token") final String token) {
        logger.info("GET");
        try {
            return getEntities(token);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response save(@HeaderParam("token") final String token, final E entity) {
        logger.info("POST");
        try {
            return saveEntity(token, entity);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response update(@HeaderParam("token") final String token, final E entity) {
        logger.info(webContextPath() + "PUT");
        try {
            return updateEntity(token, entity);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @DELETE
    @Path(PATH_DELETE)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response delete(@HeaderParam("token") final String token, @PathParam("id") final int deleteId) {
        logger.info("DELETE");
        try {
            return deleteEntity(token, deleteId);
        } catch (Exception ex) {
            return response(ex.getMessage());
        }
    }

    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    protected Response response(final Integer result) {
        return response(result.toString());
    }

    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    protected Response response(final String message) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMessage(message);
        return Response.status(200).entity(responseBean).build();
    }

    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    protected Response responseAccessDenied() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMessage("access denied");
        return Response.status(200).entity(responseBean).build();
    }

    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    protected Response responseNothingFound() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setMessage("nothing found");
        return Response.status(200).entity(responseBean).build();
    }

    protected final Response getEntitiesFromForeignKey(final String token, final int foreignKey, final IRepositoryFromForeignKey<E> fromForeignKey) {
        if (isAccessDenied(token, Rights.GET)) {
            return responseAccessDenied();
        }
        return all(fromForeignKey, foreignKey);
    }

    protected boolean isAccessDenied(final Authentication authentication) {
        if (!authentication.getUserId().isPresent() || !authentication.getToken().isPresent()) {
            return true;
        }
        final Optional<String> optionalToken = authentication.getToken();
        return optionalToken.map(Token::isAccessDenied).orElse(true);
    }

    protected boolean isAccessDenied(final String token, final Rights right) {
        final Authentication authentication = Token.getAuthentication(token);
        if (!authentication.getUserId().isPresent() || !authentication.getToken().isPresent()) {
            return true;
        }
        Optional<UserRight> optionalUserRight = userRightRepository().get(authentication.getUserId().getAsInt(), webContextPath(), right);
        //noinspection OptionalGetWithoutIsPresent
        return optionalUserRight.map(r -> Token.isAccessDenied(authentication.getToken().get(), r)).orElse(true);
    }

    protected IUserRightRepository userRightRepository() {
        return userRightRepository;
    }

    protected String webContextPath() {
        return webContextPath;
    }

    private Response saveEntity(final String token, final E entity) {
        if (isAccessDenied(token, Rights.POST)) {
            return responseAccessDenied();
        }
        return save(entity);
    }

    private Response get(final int getId) {
        final Optional<E> optionalEntity = repository.get(getId);
        return optionalEntity.isPresent()
                ? Response.status(200).entity(optionalEntity.get()).build()
                : responseNothingFound();
    }

    private Response all() {
        return Response.status(200).entity(repository.getAll()).build();
    }

    private Response all(final IRepositoryFromForeignKey<E> fromForeignKey, final int foreignKey) {
        return Response.status(200).entity(fromForeignKey.getAll(foreignKey)).build();
    }

    private Response save(final E entity) {
        return response(repository.save(entity).getUpdates());
    }

    private Response update(final E entity) {
        return response(repository.update(entity).getUpdates());
    }

    private Response delete(final int deleteId) {
        return response(repository.delete(deleteId).getUpdates());
    }


    private Response getEntity(final String token, final int getId) {
        if (isAccessDenied(token, Rights.GET)) {
            return responseAccessDenied();
        }
        return get(getId);
    }

    private Response getEntities(final String token) {
        if (isAccessDenied(token, Rights.GET)) {
            return responseAccessDenied();
        }
        return all();
    }

    private Response updateEntity(final String token, final E entity) {
        if (isAccessDenied(token, Rights.PUT)) {
            return responseAccessDenied();
        }
        return update(entity);
    }

    private Response deleteEntity(final String token, final int deleteId) {
        if (isAccessDenied(token, Rights.DELETE)) {
            return responseAccessDenied();
        }
        return delete(deleteId);
    }

}
