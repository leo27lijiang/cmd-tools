#!/usr/bin/python python
import os
import sys
import shutil
def buildworkspace(basedir, projectname, jarname, mainclass):
    targetdir = os.path.join(basedir, 'target')
    if not os.path.isdir(targetdir):
        print('Path %s error.'%(targetdir))
        return None
    dir = os.path.join(targetdir, projectname)
    if os.path.exists(dir):
        shutil.rmtree(dir)
    os.mkdir(dir)
    bins = os.path.join(dir, 'bin')
    libs = os.path.join(dir, 'lib')
    conf = os.path.join(dir, 'conf')
    os.mkdir(bins)
    os.mkdir(libs)
    createScript(bins, mainclass, projectname)
    projectmaindir = os.path.join(os.path.join(basedir, 'src'), 'main')
    sourceconfdir = os.path.join(projectmaindir, 'conf')
    shutil.copytree(sourceconfdir,conf)
    sourcelibdir = os.path.join(targetdir, 'lib')
    for f in os.listdir(sourcelibdir):
        if f.find('junit') == 0 or f.find('hamcrest') == 0:
            continue
        sourcelibfile = os.path.join(sourcelibdir, f)
        if os.path.isfile(sourcelibfile):
            shutil.copy(sourcelibfile, libs)
    shutil.copy(os.path.join(targetdir,jarname), libs)
def createScript(targetDir, mainclass, projectname):
    startup = None
    try:
        startup = open(os.path.join(targetDir,'cmd-client.sh'),'wb+')
        content = '''#!/bin/sh
# script directory
SCRIPT_PATH=$(readlink -f $0)
# bin directory, It's like /opt/appname/bin, also same with SCRIPT_PATH
BIN_DIR=$(dirname $SCRIPT_PATH)
# work directory
WORK_DIR=$(dirname $BIN_DIR)
# where is app root path in location
APP_ROOT=$WORK_DIR
# app mainclass
MAINCLASS='''+mainclass+'''
# where is app lib path in location
LIB_DIR=${APP_ROOT}'/lib'
# app conf directory
APP_CONF=${APP_ROOT}'/conf/'
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
CLASSPATH="-cp ${FILE_LIST}${APP_CONF}"
$JAVA_CMD $CLASSPATH $MAINCLASS $*'''
        startup.write(content.encode('UTF-8'))
        startup.flush()
    except IOError:
        print('IOError in create script file')
    finally:
        startup.close()
if __name__ == '__main__':
    if len(sys.argv) < 5:
        print('Usage: python build.py basedir projectname jarname mainclass')
    else:
        buildworkspace(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
