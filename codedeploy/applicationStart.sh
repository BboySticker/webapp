#!/bin/bash
#sudo service tomcat8 start
cd ~/
nohup java -jar -Dspring.profiles.active=prod ROOT.jar &