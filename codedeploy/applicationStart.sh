#!/bin/bash
#sudo service tomcat8 start
#cd ~/
#setsid java -jar -Dspring.profiles.active=prod ROOT.jar &
#java -jar -Dspring.profiles.active=prod ROOT.jar

set -e

sudo service webapp start
echo "Service started."