package nl.hans.repository;

import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import nl.hans.entities.Hans;
import nl.hans.entities.HansChild;

import java.util.UUID;
import java.util.logging.Logger;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class HansRepository {
    @PersistenceContext(unitName = "TestPersistence")
    EntityManager em;

    @Inject
    Logger logger;

    public Hans getHans(UUID id) {
        Hans hans = em.find(Hans.class, id);
        if (hans == null) {
            throw new EntityNotFoundException("Hans not found: " + id);
        }

        return hans;
    }

    public Hans create(Hans hans) {
        this.em.persist(hans);
        logger.info("Created new hans record: " + hans.getId());

        this.em.flush();

        return hans;
    }

    public Hans deleteHans(UUID id) {
        Hans hans = this.em.find(Hans.class, id);
        if (hans == null) {
            throw new EntityNotFoundException("Hans not found: " + id);
        }
        this.em.remove(hans);
        return hans;
    }

    public Hans updateHans(Hans hans) {
        try {
            logger.info("Start: HansRepository: updateHans");
            Hans mergedHans = this.em.merge(hans);
            em.flush();
            return mergedHans;
        } finally {
            logger.info("End: HansRepository: updateHans");
        }
    }

    public HansChild deleteHansChild(UUID id) {
        HansChild hansChild = this.em.find(HansChild.class, id);
        if (hansChild == null) {
            throw new EntityNotFoundException();
        }
        this.em.remove(hansChild);
        return hansChild;
    }

    public HansChild createChild(UUID hansId, HansChild hansChild) {
        Hans hans = this.getHans(hansId);
        em.persist(hansChild);
        hans.addChild(hansChild);
        em.merge(hans);
        return hansChild;
    }

    public HansChild getChild(UUID id) {
        HansChild child = em.find(HansChild.class, id);
        if (child == null) {
            throw new EntityNotFoundException("HansChild not found: " + id);
        }

        return child;
    }

    public HansChild updateChild(HansChild child) {
        HansChild mergedChild = this.em.merge(child);
        em.flush();
        return mergedChild;
    }
}

