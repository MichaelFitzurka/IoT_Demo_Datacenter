#!/bin/bash

echo "-Building deployables"

export BUILD_LOGFILE="../build.log"

echo "-- iot_datacenter_receiver"
pushd . > /dev/null
cd receiver
mvn clean install >>  $BUILD_LOGFILE
popd > /dev/null

echo "-- iot_datacenter_monitor"
pushd . > /dev/null
cd monitor
mvn clean install >>  $BUILD_LOGFILE
popd > /dev/null

echo "-- CEP Server deployable"
pushd . > /dev/null
cd Rules_CEP
mvn clean install >>  $BUILD_LOGFILE
popd > /dev/null

echo "-- Rules Server deployable"
pushd . > /dev/null
cd Rules
mvn clean install >>  $BUILD_LOGFILE
popd > /dev/null

echo "-Building all images for Fuse IoT Demo"
echo "-- Building Base Image"
pushd .
cd Base
docker build --rm -t psteiner/base .
popd

echo "-- Building Fuse Image"
pushd .
cd Fuse
docker build --rm -t psteiner/fuse .
popd

echo "-- Building docker-compose based images"
docker-compose build
