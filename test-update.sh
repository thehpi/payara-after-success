#!/bin/bash

file=hans.json
if [ "$1" = "-f" ]
then
	shift
	file=$1
	shift
fi

curl ${verbose} -X PUT -H "Content-type: application/json" -H "Accept: application/json" -d @<(expandVariables <"${file}") -s "http://localhost:58080/test-rest/rest/hans/$1"
