package nl.hans.rest;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import nl.hans.MyEjb;
import nl.hans.entities.Hans;
import nl.hans.entities.HansChild;

import java.util.UUID;

@Path("hans")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class HansResource {

    @Inject
    private MyEjb myEjb;

    @POST
    @Path("")
    public Hans hans(Hans hans) {
        hans = this.myEjb.createHans(hans);
        return hans;
    }

    @GET
    @Path("{id}")
    public Hans getHans(@PathParam("id") UUID id) {
        return myEjb.getHans(id);
    }

    @PUT
    @Path("{id}")
    public Hans update(@PathParam("id") UUID id, Hans hans) {
        hans.setId(id);
        return this.myEjb.updateHans(hans);
    }

    @DELETE
    @Path("{id}")
    public Hans deleteHans(@PathParam("id") UUID id) {
        return this.myEjb.deleteHans(id);
    }

    @POST
    @Path("{hansId}/children")
    public HansChild createHansChild(@PathParam("hansId") UUID hansId, HansChild hansChild) {
        return myEjb.createChild(hansId, hansChild);
    }

    @GET
    @Path("{hansId}/children/{id}")
    public HansChild getHansChild(@PathParam("hansId") UUID hansId, @PathParam("id") UUID id) {
        HansChild hansChild = getHans(hansId).getChild(id);
        if (hansChild == null) {
            throw new EntityNotFoundException("Child not found: " + id);
        }
        return hansChild;
    }

    @PUT
    @Path("{hansId}/children/{id}")
    public HansChild updateHansChild(@PathParam("hansId") UUID hansId, @PathParam("id") UUID id, HansChild hansChild) {
        getHansChild(hansId, id);
        hansChild.setId(id);
        return myEjb.updateChild(hansChild);
    }

    @DELETE
    @Path("{hansId}/children/{id}")
    public HansChild deleteHansChild(@PathParam("hansId") UUID hansId, @PathParam("id") UUID id) {
        getHansChild(hansId, id);
        return myEjb.deleteHansChild(id);
    }
}
