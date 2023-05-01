package nl.hans;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

import java.util.logging.Logger;

@ApplicationScoped
public class LoggerProducer {

  private static final String HANS_LOGGER_NAME = "nl.hans";

  @Produces
  public Logger getLogger(InjectionPoint ip) {
    if(null != ip.getMember()) {
      String packageName = ip.getMember().getDeclaringClass().getPackage().getName();
      return Logger.getLogger(packageName);
    }
    throw new IllegalStateException("Cannot inject a logger if there is no injection point");
  }

  public static Logger getHansLogger() {
    return Logger.getLogger(HANS_LOGGER_NAME);
  }
}
