#!/bin/bash

unset restart
unset build
nr=5
while [ -n "$*" ]
do
	case "$1" in
		-r)
			restart=1
		;;
		-b)
			build=1
		;;
		*)
			nr=$1
		;;

	esac
	shift
done

if [ -n "${build}" ]
then
	mvn clean install -DskipTests
fi

asadmin --port=${nr}4848 undeploy test-ear-1.0.0-SNAPSHOT
[ -n "${restart}" ] && (cd .. ; docker-compose restart payara ; sleep 10)
asadmin --port=${nr}4848 deploy --upload=true target/test-ear-1.0.0-SNAPSHOT.ear 
