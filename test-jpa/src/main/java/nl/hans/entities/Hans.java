package nl.hans.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@Entity
public class Hans {

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

  private String test1;

  private int value1;

  private boolean isGenerating;

  @OneToMany(
      mappedBy = "hans",
      orphanRemoval = true,
        cascade = ALL
  )
  private Collection<HansChild> children = new ArrayList<>();
  private OffsetDateTime offsetDateTime = OffsetDateTime.now(ZoneOffset.of("+02:00"));

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTest1() {
    return test1;
  }

  public void setTest1(String test1) {
    this.test1 = test1;
  }

  public int getValue1() {
    return value1;
  }

  public void setValue1(int value1) {
    this.value1 = value1;
  }

  public Collection<HansChild> getChildren() {
    return children;
  }

  public void addChild(HansChild hansChild) {
    this.children.add(hansChild);
    hansChild.setHans(this);
  }

  public void removeChild(HansChild hansChild) {
    this.children.remove(hansChild);
  }

  public void setChildren(Collection<HansChild> children) {
    this.children = children;
  }

  public boolean isGenerating() {
    return isGenerating;
  }

  public void setGenerating(boolean generating) {
    isGenerating = generating;
  }

  public OffsetDateTime getOffsetDateTime() {
    return offsetDateTime;
  }

  public void setOffsetDateTime(OffsetDateTime offsetDateTime) {
    this.offsetDateTime = offsetDateTime;
  }

  public HansChild getChild(UUID childId) {
    if (children == null) {
      return null;
    }
    for(HansChild child:children) {
      if(child.getId().equals(childId)) {
        return child;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return "Hans{"
           + "id="
           + id
           + ", test1='"
           + test1
           + '\''
           + ", value1="
           + value1
           + ", isGenerating="
           + isGenerating
           + ", nr children="
           + (children==null?0:children.size())
           + ", offsetDateTime="
           + offsetDateTime
           + '}';
  }
}
