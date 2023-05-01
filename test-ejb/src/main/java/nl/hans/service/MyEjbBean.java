package nl.hans.service;

import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import nl.hans.MyEjb;
import nl.hans.entities.Hans;
import nl.hans.entities.HansChild;
import nl.hans.repository.HansRepository;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MyEjbBean implements MyEjb {

  @Inject Logger logger;

  @Inject HansRepository hansRepository;

  @Override
  public Hans createHans(Hans hans) {
    logger.info(() -> "Start: createHans: " + hans);
    return this.hansRepository.create(hans);
  }

  @Override
  public Hans getHans(UUID id) {
    logger.info(() -> "Start: getHans: " + id);
    return hansRepository.getHans(id);
  }

  @Override
  public HansChild getChild(UUID id) {
    logger.info(() -> "Start: getChild: " + id);
    return hansRepository.getChild(id);
  }

  @Override
  public HansChild createChild(UUID hansId, HansChild hansChild) {
    logger.info(() -> "Start: createChild: " + hansId + ", child: " + hansChild);
    return this.hansRepository.createChild(hansId, hansChild);
  }

  @Override
  public Collection<HansChild> getChildren(UUID id) {
    logger.info(() -> "Start: getChildren: " + id);
    return this.hansRepository.getHans(id).getChildren();
  }

  @Override
  public Hans updateHans(Hans hans) {
    logger.info(() -> "Start: updateHans: " + hans);
    hans.getChildren().forEach(c -> c.setHans(hans));
    return hansRepository.updateHans(hans);
  }

  @Override
  public HansChild updateChild(HansChild child) {
    logger.info(() -> "Start: updateHansChild: " + child);
    return hansRepository.updateChild(child);
  }

  @Override
  public HansChild deleteHansChild(UUID id) {
    logger.info(() -> "Start: deleteHansChild: " + id);
    return hansRepository.deleteHansChild(id);
  }

  @Override
  public Hans deleteHans(UUID id) {
    logger.info(() -> "Start: deleteHans: " + id);
    return hansRepository.deleteHans(id);
  }
}
