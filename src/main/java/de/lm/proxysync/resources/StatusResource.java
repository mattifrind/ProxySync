package de.lm.proxysync.resources;

import de.lm.proxysync.services.StatusService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/status")
public class StatusResource {

    @Inject
    StatusService statusService;

    @GET
    @Path("/")
    public String getStatus() {
        String output = "Overall: " + booleanToReadable(statusService.getOverallStatus()) + "\n Slack API: " + booleanToReadable(statusService.isSlackWorking())
                + "\n Drive API: " + booleanToReadable(statusService.isDriveWorking());
        System.out.println(output);
        return output;
    }

    @GET
    @Path("/errors")
    public List<String> getErrors() {
        return statusService.getErrors();
    }

    private String booleanToReadable(boolean input) {
        return input ? "Ok" : "Error";
    }
}
