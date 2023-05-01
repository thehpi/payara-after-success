package nl.hans.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class JPAEventNotifier {
    @Inject
    Notifier notifier;

    @Transactional(Transactional.TxType.REQUIRED)
    public void notify(Object entity) {
        notifier.notifyPeers(entity);
    }

}
