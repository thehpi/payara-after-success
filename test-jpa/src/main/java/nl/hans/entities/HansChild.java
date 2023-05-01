package nl.hans.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class HansChild {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @PrePersist
    private void prePersist() {
        if (id != null) {
            return;
        }
        id = UUID.randomUUID();
    }

    @PreRemove
    private void cleanupOnDelete() {
        if (null != this.hans) {
            this.hans.removeChild(this);
        }
    }

    private String test;

    private boolean isGenerating;

    @ManyToOne
    private Hans hans;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setHans(Hans hans) {
        this.hans = hans;
    }

    public boolean isGenerating() {
        return isGenerating;
    }

    public void setGenerating(boolean generating) {
        isGenerating = generating;
    }

    @Override
    public String toString() {
        return "HansChild{" + "id=" + id + ", test='" + test + '\'' + ", isGenerating=" + isGenerating + '}';
    }
}
