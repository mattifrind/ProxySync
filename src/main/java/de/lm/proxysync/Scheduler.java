package de.lm.proxysync;

import de.lm.proxysync.services.ProxyUploaderService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Scheduler {

    @Inject
    ProxyUploaderService proxyUploaderService;

    /**
     * alle X Sekunden, kein Gleichzeitiges ausführen ("überlappen")
     */
    @Scheduled(every = "5s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    public void syncProxy(){
        proxyUploaderService.uploadProxies();
    }

}
