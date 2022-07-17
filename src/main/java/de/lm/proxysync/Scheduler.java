package de.lm.proxysync;

import de.lm.proxysync.services.ProxyUploaderService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;

@ApplicationScoped
public class Scheduler {

    @Inject
    ProxyUploaderService proxyUploaderService;

    /**
     * alle X Sekunden, kein Gleichzeitiges ausführen ("überlappen")
     */
    @Scheduled(every = "50900s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    public void syncProxy() throws GeneralSecurityException, IOException {
        proxyUploaderService.uploadProxies();
    }

}
