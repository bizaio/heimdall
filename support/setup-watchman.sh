#!/bin/bash

SCRIPT=$(readlink -f $0)
SCRIPTPATH=`dirname $SCRIPT`
PROJECT_NAME="shared"

cd $SCRIPTPATH/..

for PROJECT in $PROJECT_NAME;
do
	`which watchman` -- trigger $PROJECT/src "heimdall-$PROJECT-build" -- $SCRIPTPATH/compile-src.sh
done

