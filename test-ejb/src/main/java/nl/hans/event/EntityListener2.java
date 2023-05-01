package nl.hans.event;

import jakarta.inject.Inject;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.transaction.Transactional;
import nl.hans.utils.TxStatus;

import java.util.logging.Logger;

public class EntityListener2 {

    @Inject
    private Logger log;

    @PostRemove
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logPostRemove(Object o) {
        log.info(() -> "EntityListener2: PostRemove: " + o + ", tx status: " + TxStatus.getTxStatus());
    }

    @PostUpdate
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logPostUpdate(Object o) {
        log.info(() -> "EntityListener2: PostUpdate: " + o + ", tx status: " + TxStatus.getTxStatus());
    }
}
