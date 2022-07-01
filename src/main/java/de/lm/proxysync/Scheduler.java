package de.lm.proxysync;

import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Scheduler {

    @Inject ProxyUploader proxyUploader;

    /**
     * alle X Sekunden, kein Gleichzeitiges ausführen ("überlappen")
     */
    @Scheduled(every = "5s", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    public void syncProxy(){
        proxyUploader.uploadProxies();
    }

}
