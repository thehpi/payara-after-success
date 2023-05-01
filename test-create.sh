#!/bin/bash

. ./functions.sh

curl "$@" -X POST -H "Content-type: application/json" -H "Accept: application/json" -d@<(expandVariables <hans.json) -s "http://localhost:58080/test-rest/rest/hans"

