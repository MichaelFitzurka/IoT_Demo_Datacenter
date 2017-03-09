#!/bin/bash
#
# Little helper for the installation of Red Hat JBoss Fuse in a Docker container
#

# echo "Building deployables"
# mvn clean install

echo "Start Fuse and wait for start procedure to end"
$HOME/$FUSE_LOCATION/bin/start

while [ "`$HOME/$FUSE_LOCATION/bin/status`" != "Running ..." ]
do
echo 'Fuse not available ... waiting 5 seconds'
sleep 5
done

echo "Now let's deploy the bundle and some prereqs"
echo "commons-dbcp"
$HOME/$FUSE_LOCATION/bin/client -h 127.0.0.1 -r 60 -d 10 "osgi:install -s wrap:mvn:commons-dbcp/commons-dbcp/1.4"
echo "postgresql"
$HOME/$FUSE_LOCATION/bin/client -h 127.0.0.1 -r 60 -d 10 "osgi:install -s wrap:mvn:org.postgresql/postgresql/9.3-1102-jdbc41"
echo "camel-jetty"
$HOME/$FUSE_LOCATION/bin/client -h 127.0.0.1 -r 60 -d 10 "features:install camel-jetty"
