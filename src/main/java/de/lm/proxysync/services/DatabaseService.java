package de.lm.proxysync.services;

import de.lm.proxysync.data.IgnorePathEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class DatabaseService {

    @Inject
    EntityManager em;

    public List<String> getIgnoredPaths() {
        return em.createQuery("Select ignorePath  from IgnorePathEntity ignorePath", IgnorePathEntity.class).getResultList()
                .stream().map(IgnorePathEntity::getIgnorePath).collect(Collectors.toList());
    }

    @Transactional
    public void addIgnorePath(String ignorePath) {
        IgnorePathEntity ignorePathEntity = new IgnorePathEntity();
        ignorePathEntity.setIgnorePath(ignorePath);
        em.persist(ignorePathEntity);
    }

    @Transactional
    public void deleteIgnorePath(String ignorePath) {
        em.createNativeQuery("delete from IgnorePathEntity where ignorePath = :ignorePath")
                .setParameter("ignorePath", ignorePath)
                .executeUpdate();
    }


}
