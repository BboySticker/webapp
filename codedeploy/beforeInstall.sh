#!/bin/bash

# change to tomcat webapps directory.
# this directory will be different for different tomcat versions.
#cd /var/lib/tomcat8/webapps
#sudo rm -rf *


cd /home/ubuntu
sudo rm -rf *

#set -e
#
#CATALINA_HOME=/usr/share/tomcat8-codedeploy
#TOMCAT_VERSION=8.5.30
#
## Tar file name
#TOMCAT8_CORE_TAR_FILENAME="apache-tomcat-$TOMCAT_VERSION.tar.gz"
## Download URL for Tomcat8 core
#TOMCAT8_CORE_DOWNLOAD_URL="http://mirror.23media.de/apache/tomcat/tomcat-8/v$TOMCAT_VERSION/bin/$TOMCAT7_CORE_TAR_FILENAME"
## The top-level directory after unpacking the tar file
#TOMCAT8_CORE_UNPACKED_DIRNAME="apache-tomcat-$TOMCAT_VERSION"
#
#
## Check whether there exists a valid instance
## of Tomcat7 installed at the specified directory
#[[ -d $CATALINA_HOME ]] && { service tomcat8 status; } && {
#    echo "Tomcat8 is already installed at $CATALINA_HOME. Skip reinstalling it."
#    exit 0
#}
#
## Clear install directory
#if [ -d $CATALINA_HOME ]; then
#    rm -rf $CATALINA_HOME
#fi
#sudo mkdir -p $CATALINA_HOME
#
## Download the latest Tomcat7 version
#cd /tmp
#wget $TOMCAT8_CORE_DOWNLOAD_URL
#if [[ -d /tmp/$TOMCAT8_CORE_UNPACKED_DIRNAME ]]; then
#    rm -rf /tmp/$TOMCAT8_CORE_UNPACKED_DIRNAME
#fi
#tar xzf $TOMCAT8_CORE_TAR_FILENAME
#
## Copy over to the CATALINA_HOME
#sudo cp -r /tmp/$TOMCAT8_CORE_UNPACKED_DIRNAME/* $CATALINA_HOME
#
#sudo -s
## Create the service init.d script
#cat > /etc/init.d/tomcat8 <<'EOF'
##!/bin/bash
## description: Tomcat8 Start Stop Restart
## processname: tomcat8
#CATALINA_HOME='/usr/share/tomcat8-codedeploy'
#
#case $1 in
#start)
#sh $CATALINA_HOME/bin/startup.sh
#;;
#stop)
#sh $CATALINA_HOME/bin/shutdown.sh
#;;
#restart)
#sh $CATALINA_HOME/bin/shutdown.sh
#sh $CATALINA_HOME/bin/startup.sh
#;;
#esac
#exit 0
#EOF
#
#exit
#
## Change permission mode for the service script
#chmod 755 /etc/init.d/tomcat8
#
## Reload the
#sudo systemctl daemon-reload