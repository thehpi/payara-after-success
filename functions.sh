#!/bin/bash

expandVariables() 
{
set -u

echo "cat <<EOF
$(cat "$@")
EOF"|bash
}
