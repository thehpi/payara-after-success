package nl.hans.service;

import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Local;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import nl.hans.entities.Hans;
import nl.hans.entities.HansChild;
import nl.hans.repository.HansRepository;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

@Local
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MyEjbBean {

  @Inject Logger logger;

  @Inject HansRepository hansRepository;

  public Hans createHans(Hans hans) {
    logger.info(() -> "Start: createHans: " + hans);
    return this.hansRepository.create(hans);
  }

  public Hans getHans(UUID id) {
    logger.info(() -> "Start: getHans: " + id);
    return hansRepository.getHans(id);
  }

  public HansChild getChild(UUID id) {
    logger.info(() -> "Start: getChild: " + id);
    return hansRepository.getChild(id);
  }

  public HansChild createChild(UUID hansId, HansChild hansChild) {
    logger.info(() -> "Start: createChild: " + hansId + ", child: " + hansChild);
    return this.hansRepository.createChild(hansId, hansChild);
  }

  public Collection<HansChild> getChildren(UUID id) {
    logger.info(() -> "Start: getChildren: " + id);
    return this.hansRepository.getHans(id).getChildren();
  }

  public Hans updateHans(Hans hans) {
    logger.info(() -> "Start: updateHans: " + hans);
    hans.getChildren().forEach(c -> c.setHans(hans));
    return hansRepository.updateHans(hans);
  }

  public HansChild updateChild(HansChild child) {
    logger.info(() -> "Start: updateHansChild: " + child);
    return hansRepository.updateChild(child);
  }

  public HansChild deleteHansChild(UUID id) {
    logger.info(() -> "Start: deleteHansChild: " + id);
    return hansRepository.deleteHansChild(id);
  }

  public Hans deleteHans(UUID id) {
    logger.info(() -> "Start: deleteHans: " + id);
    return hansRepository.deleteHans(id);
  }
}
