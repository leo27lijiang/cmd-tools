#!/bin/sh
# script directory
SCRIPT_PATH=$(readlink -f $0)
# bin directory, It's like /opt/appname/bin, also same with SCRIPT_PATH
BIN_DIR=$(dirname $SCRIPT_PATH)
# work directory
WORK_DIR=$(dirname $BIN_DIR)
# where is app root path in location
APP_ROOT=$WORK_DIR
# app mainclass
MAINCLASS=com.lefu.cmdtools.client.CmdClient
# where is app lib path in location
LIB_DIR=${APP_ROOT}'/lib'
# app all jar file path 
FILE_LIST=''
for filename in `ls $LIB_DIR`
        do
                if [ -f $LIB_DIR'/'$filename ] ; then
                        FILE_LIST=${FILE_LIST}${LIB_DIR}'/'$filename':'
                fi
        done
if [ "$JAVA_HOME" = "" ]; then
        echo "JAVA_HOME not found."
        exit 1
fi
JAVA_CMD=${JAVA_HOME}'/bin/java'
CLASSPATH="-cp ${FILE_LIST}"
$JAVA_CMD $CLASSPATH $MAINCLASS $*
