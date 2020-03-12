#!/bin/bash
set -e

sudo service tomcat8 stop
sudo service webapp restart
echo "Service started."