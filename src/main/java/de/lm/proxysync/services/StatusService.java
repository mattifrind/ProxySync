package de.lm.proxysync.services;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class StatusService {

    private boolean slackWorking = false;
    private boolean driveWorking = false;

    private List<String> errors;

    public boolean getOverallStatus() {
        return slackWorking && driveWorking;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isSlackWorking() {
        return slackWorking;
    }

    public void setSlackWorking(boolean slackWorking) {
        this.slackWorking = slackWorking;
    }

    public boolean isDriveWorking() {
        return driveWorking;
    }

    public void setDriveWorking(boolean driveWorking) {
        this.driveWorking = driveWorking;
    }
}
