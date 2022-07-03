package de.lm.proxysync.ressources;

import de.lm.proxysync.services.DatabaseService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("/ignoredpaths")
public class StatusRessource {

    @Inject
    DatabaseService databaseService;

    @GET
    @Path("/")
    public List<String> getIgnoredPaths() {
        return databaseService.getIgnoredPaths();
    }

    @PUT
    @Path("/{path}")
    public void addIgnorePath(@PathParam("path") String path) {
        databaseService.addIgnorePath(path);
    }

    @DELETE
    @Path("/{path}")
    public void deleteIgnorePath(@PathParam("path") String path) {
        databaseService.deleteIgnorePath(path);
    }

}
