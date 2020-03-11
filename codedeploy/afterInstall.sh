#!/bin/bash

# Fixing permission and attach ownership to tomcat
# Not 777

#sudo groupadd tomcat8
#sudo useradd -s /bin/false -g tomcat8 -d /var/lib/tomcat8 tomcat8
#
#cd /var/lib/tomcat8
## set proper permissions
#sudo chgrp -R tomcat8 conf
#sudo chmod g+rwx conf
#sudo chmod g+r conf/*
#
## set ownerships
#sudo chown -R tomcat8 work/ logs/ webapps/

set -e

chown webapp-user:webapp-user /var/webapp/ROOT.jar

# protect application from modifications
chmod 555 /var/webapp/ROOT.jar
#chattr +i /var/sample-app/sample-app.jar

if [[ -f /etc/init.d/webapp ]]; then
    sudo rm /etc/init.d/webapp
fi

# create symlink to init.d
#ln -s /var/webapp/ROOT.jar /etc/init.d/webapp
cat > /etc/init.d/webapp <<'EOF'
#!/bin/bash

case $1 in
start)
setsid java -jar -Dspring.profiles.active=prod /var/webapp/ROOT.jar &
;;
stop)
pkill java
;;
esac
exit 0
EOF
update-rc.d webapp defaults
echo "Service installed."


#CATALINA_HOME='/usr/share/tomcat8-codedeploy'
#DEPLOY_TO_ROOT='true'
##CONTEXT_PATH='##CONTEXT_PATH##'
#SERVER_HTTP_PORT='8080'
#
#TEMP_STAGING_DIR='~'
#WAR_STAGED_LOCATION="$TEMP_STAGING_DIR/ROOT.war"
##HTTP_PORT_CONFIG_XSL_LOCATION="$TEMP_STAGING_DIR/configure_http_port.xsl"
#
## In Tomcat, ROOT.war maps to the server root
#if [[ "$DEPLOY_TO_ROOT" = 'true' ]]; then
#    CONTEXT_PATH='ROOT'
#fi
#
## Remove unpacked application artifacts
#if [[ -f $CATALINA_HOME/webapps/$CONTEXT_PATH.war ]]; then
#    rm $CATALINA_HOME/webapps/$CONTEXT_PATH.war
#fi
#if [[ -d $CATALINA_HOME/webapps/$CONTEXT_PATH ]]; then
#    rm -rfv $CATALINA_HOME/webapps/$CONTEXT_PATH
#fi
#
## Copy the WAR file to the webapps directory
#cp $WAR_STAGED_LOCATION $CATALINA_HOME/webapps
#
## Configure the Tomcat server HTTP connector
##{ which xsltproc; } || { yum install xsltproc; } || { apt-get install xsltproc; }
##cp $CATALINA_HOME/conf/server.xml $CATALINA_HOME/conf/server.xml.bak
##xsltproc $HTTP_PORT_CONFIG_XSL_LOCATION $CATALINA_HOME/conf/server.xml.bak > $CATALINA_HOME/conf/server.xml
#
#service tomcat8 start
