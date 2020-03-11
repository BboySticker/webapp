#!/bin/bash

# stop tomcat service
sudo service tomcat8 stop

set -e

if (( $(ps -ef | grep -v grep | grep webapp | wc -l) > 0 ))
then
    service webapp stop
    echo "Service stopped."
fi
