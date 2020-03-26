#!/bin/bash

set -e

# stop tomcat service
sudo service tomcat8 stop

if (( $(ps -ef | grep -v grep | grep webapp | wc -l) > 0 ))
then
    sudo service webapp stop
    echo "Service stopped."
fi
