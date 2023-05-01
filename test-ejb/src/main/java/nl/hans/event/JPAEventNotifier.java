package nl.hans.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JPAEventNotifier {
    @Inject
    Notifier notifier;

    @Transactional(Transactional.TxType.REQUIRED)
    public void postUpdate(Object entity) {
        notifier.notifyPeers(entity);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void postRemove(Object entity) {
        notifier.notifyPeers(entity);
    }
}
