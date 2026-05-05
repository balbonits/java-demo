#!/bin/bash
set -e

URL="https://start.spring.io/starter.zip"
PARAMS="type=maven-project&language=java&bootVersion=3.5.0&baseDir=."
PARAMS="$PARAMS&groupId=com.example&artifactId=mediaops&name=mediaops"
PARAMS="$PARAMS&packageName=com.example.mediaops&javaVersion=17"
PARAMS="$PARAMS&dependencies=web,data-jpa,postgresql,validation,actuator"

curl -sS "${URL}?${PARAMS}" -o starter.zip
unzip -q starter.zip
rm starter.zip
ls -la
