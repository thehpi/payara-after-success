#!/bin/bash

curl -v -X DELETE -s "http://localhost:58080/test-rest/rest/hans/$1/children/$2"
