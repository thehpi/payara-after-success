#!/bin/bash

nr=${1:-4}

asadmin --port=${nr}4848 add-library --upload=true ${0%/*}/postgresql-42.6.0.jar

