#!/bin/bash

nr=${1:-5}

dir=${0%/*}

${dir}/deploy_postgres_driver.sh "${nr}"

${dir}/unregister_dbTestDatabase.sh "${nr}"
${dir}/register_dbTestDatabase.sh "${nr}"

