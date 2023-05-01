package nl.hans.utils;

import jakarta.transaction.TransactionSynchronizationRegistry;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TxStatus {
    private static TransactionSynchronizationRegistry registry;

    enum Status {
        STATUS_ACTIVE, STATUS_MARKED_ROLLBACK, STATUS_PREPARED, STATUS_COMMITTED, STATUS_ROLLEDBACK, STATUS_UNKNOWN, STATUS_NO_TRANSACTION, STATUS_PREPARING, STATUS_COMMITTING, STATUS_ROLLING_BACK;
    }

    public static String getTxStatus() {
        try {
            if (registry == null) {
                registry = (TransactionSynchronizationRegistry) (new InitialContext()).lookup("java:comp/TransactionSynchronizationRegistry");
            }
            if (registry == null) {
                return "NO REGISTRY";
            }

            return Status.values()[registry.getTransactionStatus()].name() + ", Key: " + registry.getTransactionKey();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
