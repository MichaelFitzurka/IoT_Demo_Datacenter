#!/bin/bash

echo "Copy $BUNDLE_NAME for hot deployment -> don't do that in production!"
cp $HOME/target/$BUNDLE_NAME $HOME/$FUSE_LOCATION/deploy

# Start Fuse
$HOME/$FUSE_LOCATION/bin/fuse server
