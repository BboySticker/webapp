#!/bin/bash

set -e

sudo chown webapp-user:webapp-user /var/webapp/ROOT.jar /var/webapp/ROOT.conf

# protect application from modifications
sudo chmod 555 /var/webapp/ROOT.jar
sudo chmod 755 /var/webapp/ROOT.conf
#chattr +i /var/sample-app/sample-app.jar

if [[ -f /etc/init.d/webapp ]]; then
    sudo rm /etc/init.d/webapp
fi

# create symlink to init.d
sudo ln -s /var/webapp/ROOT.jar /etc/init.d/webapp

sudo chmod 755 /etc/init.d/webapp
sudo update-rc.d webapp defaults
echo "Service installed."
