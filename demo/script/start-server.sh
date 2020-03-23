#!/bin/bash
CURRENT_DIR=$PWD
cd ..
PATENT_PATH=$PWD

CLASS_PATH=`mvn dependency:build-classpath | grep "Dependencies classpath" -A 1 | sed -n '2p'`

for f in $PATENT_PATH/target/*.jar;do
	CLASS_PATH=$CLASS_PATH:$f;
done

#for f in $PATENT_PATH/lib/*.jar;do
#CLASS_PATH=$CLASS_PATH:$f;
#done

java -cp $CLASS_PATH  org.rayson.demo.simple.server.SimpleServer $*
cd $CURRENT_DIR
