#!/bin/bash

export CLASSPATH='./bin/gate.jar:./lib/ant.jar:./lib/ant-launcher.jar:./lib/aopalliance.jar:./lib/apache-mime4j-core.jar:./lib/apache-mime4j-dom.jar:./lib/bcmail-jdk15.jar:./lib/bcprov-jdk15.jar:./lib/commons-codec.jar:./lib/commons-compress.jar:./lib/commons-io.jar:./lib/commons-lang.jar:./lib/commons-logging.jar:./lib/dom4j.jar:./lib/flying-saucer-core.jar:./lib/fontbox.jar:./lib/gate-asm.jar:./lib/gate-compiler-jdt.jar:./lib/ivy.jar:./lib/java-getopt.jar:./lib/jaxen.jar:./lib/jdom.jar:./lib/jempbox.jar:./lib/junit.jar:./lib/log4j.jar:./lib/log4j-1.2.17.jar:./lib/lucene-core.jar:./lib/metadata-extractor.jar:./lib/nekohtml.jar:./lib/pdfbox.jar:./lib/poi.jar:./lib/poi-ooxml.jar:./lib/poi-ooxml-schemas.jar:./lib/poi-scratchpad.jar:./lib/spring-aop.jar:./lib/spring-beans.jar:./lib/spring-core.jar:./lib/stax2-api.jar:./lib/tika-core.jar:./lib/tika-parsers.jar:./lib/woodstox-core-lgpl.jar:./lib/xercesImpl.jar:./lib/xmlbeans.jar:./lib/xmlunit.jar:./lib/xpp3.jar:./lib/xstream.jar:.' 

javac BatchProcessApp.java
javac Parse.java

java BatchProcessApp -g state.gap  ./data/diabetes.txt

javac Parse diabetes.txt.out.xml diabetes > diabetes.xml
