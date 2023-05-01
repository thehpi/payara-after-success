#!/bin/bash

nr=${1:-5}

export DBCONF="user=hans:password=hans:databaseName=hans:serverName=db:portNumber=5432:socketTimeout=5:connectTimeout=1:loginTimeout=10:tcpKeepAlive=false"

cat <<EOF|grep -v "^#" | asadmin --port=${nr}4848
create-jdbc-connection-pool --datasourceclassname org.postgresql.xa.PGXADataSource --restype javax.sql.XADataSource --property ${DBCONF} dbTestDatabase
set resources.jdbc-connection-pool.dbTestDatabase.is-connection-validation-required=false
set resources.jdbc-connection-pool.dbTestDatabase.connection-creation-retry-attempts=4
set resources.jdbc-connection-pool.dbTestDatabase.connection-creation-retry-interval-in-seconds=1
set resources.jdbc-connection-pool.dbTestDatabase.fail-all-connections=false

set resources.jdbc-connection-pool.dbTestDatabase.pool-resize-quantity=2
set resources.jdbc-connection-pool.dbTestDatabase.max-pool-size=32
set resources.jdbc-connection-pool.dbTestDatabase.steady-pool-size=8
set resources.jdbc-connection-pool.dbTestDatabase.idle-timeout-in-seconds=60

create-jdbc-resource --connectionpoolid dbTestDatabase jdbc/dbTestDatabase

create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGSimpleDataSource --restype javax.sql.DataSource --property ${DBCONF} dbTestDatabase2
set resources.jdbc-connection-pool.dbTestDatabase2.is-connection-validation-required=false
set resources.jdbc-connection-pool.dbTestDatabase2.connection-creation-retry-attempts=4
set resources.jdbc-connection-pool.dbTestDatabase2.connection-creation-retry-interval-in-seconds=1
set resources.jdbc-connection-pool.dbTestDatabase2.fail-all-connections=false

set resources.jdbc-connection-pool.dbTestDatabase2.pool-resize-quantity=2
set resources.jdbc-connection-pool.dbTestDatabase2.max-pool-size=32
set resources.jdbc-connection-pool.dbTestDatabase2.steady-pool-size=8
set resources.jdbc-connection-pool.dbTestDatabase2.idle-timeout-in-seconds=60

create-jdbc-resource --connectionpoolid dbTestDatabase2 jdbc/dbTestCache

set server.thread-pools.thread-pool.http-thread-pool.max-thread-pool-size=119
set server.thread-pools.thread-pool.http-thread-pool.min-thread-pool-size=119

EOF
