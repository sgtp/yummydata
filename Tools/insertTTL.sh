#!/bin/bash

#Usage: give a TTL file as the first argument.

java -classpath lib/commons-logging-1.1.1.jar:lib/log4j-1.2.6.jar:lib/openrdf-sesame-2.6.5-onejar.jar:lib/sesame-http-client-2.6.5.jar:lib/slf4j-api-1.5.0.jar:lib/slf4j-jdk14-1.5.0.jar:lib/commons-codec-1.4.jar:lib/commons-httpclient-3.1.jar:bin org.yummydata.store.InsertTTL $*
