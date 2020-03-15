#!/bin/bash

set -e

# delete user if exists
user_exists=$(id -u webapp-user > /dev/null 2>&1; echo $?)
if [ $user_exists -eq 0 ]; then
    sudo userdel webapp-user
fi
# create app user
sudo useradd --shell /sbin/nologin --system --user-group webapp-user

if [ -d /var/webapp ]; then
    sudo rm -rf /var/webapp
fi
# create app directory
sudo mkdir -p /var/webapp

# Clear ROOT.conf config file
if [[ -f /var/webapp/ROOT.conf ]]; then
    sudo rm -rf /var/webapp/ROOT.conf
fi

sudo touch /var/webapp/ROOT.conf
sudo chmod 777 /var/webapp/ROOT.conf
sudo cat /env/properties >> /var/webapp/ROOT.conf

sudo chown webapp-user /var/webapp
sudo chgrp webapp-user /var/webapp
