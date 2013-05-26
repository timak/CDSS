#!/bin/bash

rm -rf diabetes.xml

javac Parse.java

echo '<records>' >> diabetes.xml

for FILE in `echo mini/*xml`
do
echo "processing $FILE ..."

java Parse $FILE mass plas age preg pres pedi insu skin diagnoze >> diabetes.xml
done

echo '</records>' >> diabetes.xml
