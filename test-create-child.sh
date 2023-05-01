curl -X POST -H "Content-type: application/json" -H "Accept: application/json" -d@<(expandVariables <hanschild.json) -s "http://localhost:58080/test-rest/rest/hans/$1/children"
