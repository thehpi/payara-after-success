package nl.hans.event;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.PostRemove;
import nl.hans.utils.TxStatus;

import java.util.logging.Logger;

public class EntityListener {

    @Inject
    private Logger log;

    @Inject
    @PostPersistanceEvent
    private Event<Object> event;

    @PostRemove
    public void logPostRemove(Object o) {
        log.info(() -> "EntityListener: PostRemove: " + o + ", tx status: " + TxStatus.getTxStatus());
        event.fire(o);
    }

}
