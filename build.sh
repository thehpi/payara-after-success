#!/bin/bash

trap "echo ERROR ; exit 1" ERR INT

deploy=1
while [ -n "$*" ]
do
	case "$1" in
		-nd)
			unset deploy
		;;
		*)
			echo "Unknown option: $1" >&2
			exit 1
		;;
	esac
	shift
done

mvn clean install -DskipTests

cd test-jpa
./build.sh 5
cd - >/dev/null

