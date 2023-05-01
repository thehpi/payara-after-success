#!/bin/bash

if [ "$1" = "-v" ]
then
	verbose=$1
	shift
fi

curl ${verbose}  -H "Accept: application/json" -s "http://localhost:58080/test-rest/rest/hans/$1"
