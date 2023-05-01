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

    public void postEntityManagerAction(@Observes(during = TransactionPhase.AFTER_SUCCESS) @PostPersistanceEvent Object entity) {
        logger.info(() -> "JPAEventListener: PostPersistanceEvent: " + entity + " : tx status: " + TxStatus.getTxStatus());
        jpaEventNotifier.notify(entity);
    }

}
