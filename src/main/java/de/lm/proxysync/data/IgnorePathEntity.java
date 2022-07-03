package de.lm.proxysync.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class IgnorePathEntity {

    private String ignorePath;
    private Long id;

    public IgnorePathEntity(String ignorePath) {
        this.ignorePath = ignorePath;
    }

    public IgnorePathEntity() {

    }

    public String getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(String ignorePath) {
        this.ignorePath = ignorePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @SequenceGenerator(name = "ignorePathSeq", sequenceName = "ignorePathSeq_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "ignorePathSeq")
    public Long getId() {
        return id;
    }
}
