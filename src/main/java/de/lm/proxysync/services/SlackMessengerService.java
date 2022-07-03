package de.lm.proxysync.services;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.io.IOException;

@ApplicationScoped
@Startup
public class SlackMessengerService {

    @ConfigProperty(name = "SLACK_TOKEN")
    String token;

    private Slack slack;

    private void initSlackApp() {
        slack = Slack.getInstance();
        try {
            slack.methods(token).conversationsJoin(req -> req.channel("C03N7D60LN8"));
            sendMessage(":wave: Hi from the ProxySync-Bot. I'm going to post here after I upload proxies for a new project.");
        } catch (IOException | SlackApiException e) {
            System.err.println("Failed to join proxy-sync channel");
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg) {
        ChatPostMessageResponse response = null;
        try {
            response = slack.methods(token).chatPostMessage(req -> req
                    .channel("C03N7D60LN8")
                    .text(msg));
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
        if (response != null) {
            if (response.isOk()) {
                Message postedMessage = response.getMessage();
                System.out.println(postedMessage);
            } else {
                System.err.println("Failed to join proxy-sync channel");
            }
        }
    }

    public void sendMessageForNewProject(String projectName, int filesCount) {
        String NEW_PROJECT_PRE_TEXT = ":neu: Uploaded new project";
        //sendMessage(NEW_PROJECT_PRE_TEXT + " \"" + projectName + "\" with " + filesCount + " files.");
    }

    public void onStart(@Observes StartupEvent ev) {
        initSlackApp();
    }
}
