# Reproducer for Payara AFTER_SUCCESS

## Problem Description

This project has a rest api by which a parent record and child records can be created and deleted. 

When data is changed then other parties must be notified. This is done using the notification mechanism of postgresql.
Other parties can consume such notifications and handle them.

It uses an entity listener to notice e.g. that a record was removed.

All code below is not complete, I left out boilerplate code for readability. See the source code.

    public class EntityListener {
        @Inject
        @PostRemoveEvent
        private Event<Object> removeEvent;
    
        @PostRemove
        public void logPostRemove(Object o) {
            log.info(() -> "EntityListener: PostRemove: " + o + ", tx status: " + TxStatus.getTxStatus());
            removeEvent.fire(o);
        }
 }

We only want to send such notifications when the commit is successful, so we use this observer method:

    public class JPAEventListener {
    @Inject
    Logger logger;
    
        @Inject
        JPAEventNotifier jpaEventNotifier;
    
        public void postRemove(@Observes(during = TransactionPhase.AFTER_SUCCESS) @PostRemoveEvent Object entity) {
            logger.info(() -> "JPAEventListner: PostRemoveEvent: " + entity + " : tx status: " + TxStatus.getTxStatus());
            jpaEventNotifier.postRemove(entity);
        }
    }

The AFTER_SUCCESS makes sure its only called when the transaction is committed.

The JPAEventNotifier is then used to do the handling

    public class JPAEventNotifier {
        @Inject
        Notifier notifier;
    
        @Transactional(Transactional.TxType.REQUIRED)
        public void postRemove(Object entity) {
            notifier.notifyPeers(entity);
        }
    }

We want to be sure this is handled inside a transaction, hence the REQUIRED annotation.

The Notifier will create the connection from a datasource:

    public Notifier() {
        public void notifyPeers(Object entity) {
            try (final Connection conn = dataSource.getConnection()) {
                if (conn.isWrapperFor(PGConnection.class)) {
                    try (Statement stm = conn.createStatement()) {
                        stm.execute(".....");
                    }
                }
            }
        }
    }

This all works great. Until we added another entity listener (just an example) :

    public class EntityListener2 {
        @PostRemove
        @Transactional(Transactional.TxType.REQUIRES_NEW)
        public void logPostRemove(Object o) {
            log.info(() -> "EntityListener2: PostRemove: " + o + ", tx status: " + TxStatus.getTxStatus());
        }
    }

The REQUIRES_NEW is used because we want this action to be part of a separate transaction (I know its only logging, it's just an example to make my point).

When deleting a record, **EntityListener2** is called first and works ok. Then, **EntityListener** is called which sends the event.
When the transaction is committed the **JPAEventListener.postRemove** method is called.
Then **JPAEventNotifier.postRemove** is called which calls **Notifier.notifyPeers**.
The transaction however is already committed so the getConnection() blows up with the following exception:

    RAR5029:Unexpected exception while registering component
    java.lang.IllegalStateException: Transaction is not active in the current thread.
    at com.sun.enterprise.transaction.JavaEETransactionImpl.checkTransationActive(JavaEETransactionImpl.java:756)
    at com.sun.enterprise.transaction.JavaEETransactionImpl.enlistResource(JavaEETransactionImpl.java:692)
    at com.sun.enterprise.transaction.jts.JavaEETransactionManagerJTSDelegate.enlistDistributedNonXAResource(JavaEETransactionManagerJTSDelegate.java:298)
    at com.sun.enterprise.transaction.JavaEETransactionManagerSimplified.enlistResource(JavaEETransactionManagerSimplified.java:469)
    at com.sun.enterprise.resource.rm.ResourceManagerImpl.registerResource(ResourceManagerImpl.java:152)
    at com.sun.enterprise.resource.rm.ResourceManagerImpl.enlistResource(ResourceManagerImpl.java:112)
    at com.sun.enterprise.resource.pool.PoolManagerImpl.getResource(PoolManagerImpl.java:211)
    at com.sun.enterprise.connectors.ConnectionManagerImpl.getResource(ConnectionManagerImpl.java:360)
    at com.sun.enterprise.connectors.ConnectionManagerImpl.internalGetConnection(ConnectionManagerImpl.java:307)
    at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:196)
    at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:171)
    at com.sun.enterprise.connectors.ConnectionManagerImpl.allocateConnection(ConnectionManagerImpl.java:166)
    at com.sun.gjc.spi.base.AbstractDataSource.getConnection(AbstractDataSource.java:113)

## Prerequisites ##

You need

* a mac (this was only tested on a mac)
* jdk 11
* maven
* docker

## Building and starting the application

Build the application

    mvn clean install -DskipTests

Start the docker environment and application

    ./restart.sh

When successful you can create records using scripts:

* test-create.sh: uses hans.json as input
* test-get.sh <parent id>
* test-update.sh <parent id> : uses hans.json as input
* test-delete.sh <parent id>
* test-create-child.sh <parent id> : uses hanschild.json as input
* test-delete-child.sh <parent id> <child id>

## Steps to Reproduce

* start the environment
* create a parent record
  * ./test-create.sh
* delete a parent record
  * get the id from the response when you created it
  * ./test-delete.sh <parent id>

If you now check the logs you will see the exception which was described above

    docker logs --follow payara-after-success-payara-1

## Other Error when reverting REQUIRED and REQUIRES_NEW

If **EntityLister2** is changed to use REQUIRED then it works fine, so it seems that when a new transaction is created this is causing the issue.
If **EntityLister2** is changed to use REQUIRED but **JPAEventNotifier** is changed to use REQUIRES_NEW then the big stacktrace is not shown but
a WELD error occurs:

  [#|2023-05-01T17:22:09.340+0000|ERROR|Payara 6.2023.2|org.jboss.weld.Event|_ThreadID=77;_ThreadName=http-thread-pool::http-listener-1(4);_TimeMillis=1682961729340;_LevelValue=1000;|
  WELD-000401: Failure while notifying an observer [BackedAnnotatedMethod] public nl.hans.event.JPAEventListener.postRemove(@Observes @PostRemoveEvent Object) of event null.
  jakarta.transaction.InvalidTransactionException: Invalid transaction passed to resume() call.|#]




