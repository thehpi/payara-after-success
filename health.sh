#!/bin/bash

responseStatus=$(curl -k -s "localhost:58080/health" | jq -r .status 2>/dev/null || true)
while [ "UP" != "${responseStatus}" ]
do
	echo -n "${responseStatus} "
	sleep 1
	responseStatus=$(curl -k -s "localhost:58080/health" | jq -r .status 2>/dev/null || true)
done
echo "UP"
