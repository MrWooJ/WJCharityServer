#!/bin/bash
clear
LIBPATH=""
PROJECTDIR=$(pwd)
SRCDIR=$PROJECTDIR"/src"
SRCSERVERDIR=$PROJECTDIR"/src/com/httpserver/core"
BINDIR=$PROJECTDIR"/bin"
RUNPATH="com.httpserver.core.TestServer"

function GenerateLibrariPath {
	local DIR=$PROJECTDIR"/JarFiles/"
	local JXL=$DIR"jxl.jar"
	local LUCENEANALYZER=$DIR"lucene-analyzers-3.6.2.jar"
	local COLANG=$DIR"org-apache-commons-lang.jar"
	local MYSQL=$DIR"mysql-connector-java-5.1.23-bin.jar"
	local POI=$DIR"poi-3.9.jar"
	local SWINGX=$DIR"swingx-all-1.6.4.jar"
	local LUCENECORE=$DIR"lucene-core-3.6.2.jar"
	local COMMONIO=$DIR"commons-io-2.4.jar"
	local JAVAFX=$DIR"javafx-ui-swing.jar"
	local JSONSIMPLE=$DIR"json-simple-1.1.jar"
	LIBPATH=.:$JXL:$LUCENEANALYZER:$COLANG:$POI:$SWINGX:$LUCENECORE:$MYSQL:$COMMONIO:$JAVAFX:$JSONSIMPLE:$BINDIR
}
GenerateLibrariPath
#echo "LibraryPath: "$LIBPATH

cd $SRCSERVERDIR
#echo "SRC PWD: "$(pwd)
echo "[COMPILING START]"
javac -d $BINDIR -cp $LIBPATH *.java
#echo "[COMPILING END]"

cd $PROJECTDIR
#echo "BIN PWD: "$(pwd)
java -cp $LIBPATH $RUNPATH $PROJECTDIR
