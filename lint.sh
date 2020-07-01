#!/bin/bash

./node_modules/.bin/eslint capstone/src/.

sudo update-alternatives --set java /usr/lib/jvm/java-11-openjdk-amd64/bin/java

FILES=`find . -name "*.java" -type f`
echo "Found the following Java files:"
for file in $FILES; do
  echo $file
done
echo ""
for file in $FILES; do
  echo -e "Running google-java-format on:\n $file\n"
  java -jar ./google-java-format-1.8-all-deps.jar $file | diff $file - 
done

sudo update-java-alternatives -s java-1.8.0-openjdk-amd64 && export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre
