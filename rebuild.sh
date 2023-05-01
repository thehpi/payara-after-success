#!/bin/bash

docker-compose down
docker-compose up -d
sleep 5
./build.sh -nd
asadmin --port=54848  set-log-levels java.util.logging.ConsoleHandler=FINE
./redeploy.sh

