#!/bin/bash
#sudo service tomcat8 start
cd ~/
sudo setsid java -jar -Dspring.profiles.active=prod ROOT.jar &