#!/bin/bash

# Fixing permission and attach ownership to tomcat
# Not 777
sudo groupadd tomcat
sudo useradd -s /bin/false -g tomcat -d /var/lib/tomcat8 tomcat
export CATALINA_HOME=/var/lib/tomcat8

cd /var/lib/tomcat8
# set proper permissions
sudo chgrp -R tomcat conf
sudo chmod g+rwx conf
sudo chmod g+r conf/*

# set ownerships
sudo chown -R tomcat work/ logs/ webapps/