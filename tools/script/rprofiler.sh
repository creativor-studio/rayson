#!/bin/bash

########## A script used to test and profile Rayson. #############

#current working  directory.

CurrentDir=$(cd `dirname $0`; pwd);
cd ..
PWD=`pwd`;
#maven target path
declare targetPath=$PWD/target;


# JVM arguments.
#declare JVM_ARGS="-Drayson.net.debug=ssl";

#java debug option
#declare DEBUG_OPTION="-Xdebug  -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044";

#load jprofiler agent
#declare PROFILER_OPTION="-agentpath:/Applications/JProfiler.app/Contents/Resources/app/bin/macos/libjprofilerti.jnilib"

#Load java libraries.
function loadLibs(){
	
	classPath=`mvn dependency:build-classpath | grep "Dependencies classpath" -A 1 | sed -n '2p'`
	classPath=$classPath:${targetPath}/classes
	#load jars from target root directory
#	for jar in ${targetPath}/*.jar ; do
#		classPath=$classPath:$jar;
#	done;
}

function convertPath(){
	if [ "cygwin" = "${OSTYPE}" ]; then
	  classPath=`cygpath -p -w "${classPath}"`;
	fi;
}

#Program main entry.
function Main(){
	loadLibs;
	convertPath;
	#run java shell.
        java $JVM_ARGS $DEBUG_OPTION $PROFILER_OPTION -cp $classPath org.rayson.tools.profiler.Console $@;
}


Main $@;

cd ${CurrentDir}
