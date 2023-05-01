package nl.hans;

import jakarta.ejb.Local;
import nl.hans.entities.Hans;
import nl.hans.entities.HansChild;

import java.util.Collection;
import java.util.UUID;

@Local
public interface MyEjb {
    Hans createHans(Hans hans);
    Hans deleteHans(UUID id);
    Hans getHans(UUID id);
    Hans updateHans(Hans hans);
    HansChild createChild(UUID id, HansChild hansChild);
    HansChild deleteHansChild(UUID id);
    HansChild getChild(UUID id);
    HansChild updateChild(HansChild hansChild);
    Collection<HansChild> getChildren(UUID id);
}
