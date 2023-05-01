package nl.hans.event;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import nl.hans.utils.TxStatus;

import java.util.logging.Logger;

public class EntityListener {

    @Inject
    private Logger log;

    @Inject
    @PostRemoveEvent
    private Event<Object> removeEvent;

    @Inject
    @PostUpdateEvent
    private Event<Object> updateEvent;

    @PostRemove
    public void logPostRemove(Object o) {
        log.info(() -> "EntityListener: PostRemove: " + o + ", tx status: " + TxStatus.getTxStatus());
        removeEvent.fire(o);
    }

    @PostUpdate
    public void logPostUpdate(Object o) {
        log.info(() -> "EntityListener: PostUpdate: " + o + ", tx status: " + TxStatus.getTxStatus());
        updateEvent.fire(o);
    }
}
