#!/bin/bash
#sudo service tomcat8 start
cd ~/
echo "FIRST: setsid java -jar -Dspring.profiles.active=prod ROOT.jar &"
setsid java -jar -Dspring.profiles.active=prod ROOT.jar &
echo "SECOND: setsid java -jar -Dspring.profiles.active=prod ROOT.jar &"