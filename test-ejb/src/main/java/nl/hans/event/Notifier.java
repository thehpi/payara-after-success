package nl.hans.event;

import nl.hans.utils.TxStatus;
import org.postgresql.PGConnection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Notifier {

  Logger logger = Logger.getLogger(this.getClass().getName());

  private static final String DATASOURCE_JNDI_NAME = "jdbc/dbTestCache";

  private DataSource dataSource;

  public Notifier() {
    try {
      dataSource = InitialContext.doLookup(DATASOURCE_JNDI_NAME);
    } catch (final NamingException e) {
      logger.severe("Could not construct Notifier");
      throw new IllegalStateException("Unable to start application");
    }
  }

  public void notifyPeers(Object entity) {
    try (final Connection conn = dataSource.getConnection()) {
      logger.info(() -> "Get Connection worked fine. Entity: " + entity + ", tx status: " + TxStatus.getTxStatus());
      if (conn.isWrapperFor(PGConnection.class)) {
        try (Statement stm = conn.createStatement()) {
          stm.execute("select 1");
        }
      }

    } catch (SQLException e) {
      logger.severe("Get Connection FAILED. Entity: " + entity + " tx status: " + TxStatus.getTxStatus());
    }
  }
}
