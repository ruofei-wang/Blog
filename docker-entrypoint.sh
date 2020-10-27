#!/bin/bash
exec java -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/gc-$(date +%F).log -Dfile.encoding=utf-8 -jar Blog.jar --spring.config.location=application.yml
