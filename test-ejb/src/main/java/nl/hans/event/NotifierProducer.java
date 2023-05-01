package nl.hans.event;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@Singleton
@Startup
public class NotifierProducer {

    Notifier notifier;

    @PostConstruct
    public void setup() {
        this.notifier = new Notifier();
    }

    @Produces
    @ApplicationScoped
    public Notifier getNotifier() {
        return notifier;
    }

}
