package nl.hans.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;
import nl.hans.utils.TxStatus;

import java.util.logging.Logger;

@ApplicationScoped
public class JPAEventListener {
    @Inject
    Logger logger;

    @Inject
    JPAEventNotifier jpaEventNotifier;

    public void postUpdate(@Observes(during = TransactionPhase.AFTER_SUCCESS) @PostUpdateEvent Object entity) {
        logger.info(() -> "JPAEventListener: PostUpdateEvent: " + entity + " : tx status: " + TxStatus.getTxStatus());
        jpaEventNotifier.postUpdate(entity);
    }

    public void postRemove(@Observes(during = TransactionPhase.AFTER_SUCCESS) @PostRemoveEvent Object entity) {
        logger.info(() -> "JPAEventListner: PostRemoveEvent: " + entity + " : tx status: " + TxStatus.getTxStatus());
        jpaEventNotifier.postRemove(entity);
    }

}
