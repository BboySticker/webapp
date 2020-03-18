#!/bin/bash
set -e

sudo service tomcat8 stop
sudo service webapp restart
echo "Service started."

# running cloud watch agent
sudo cp /home/ubuntu/cloudwatch-config.json /opt
sudo systemctl enable amazon-cloudwatch-agent.service
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/cloudwatch-config.json -s
sudo systemctl restart amazon-cloudwatch-agent