@echo off
title "stop mqlist"
java -Dfile.encoding=GBK -jar mqtask-0.0.1.RELEASE.jar -f mqlist-example.json -o stop
pause
