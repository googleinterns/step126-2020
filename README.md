# Google Student Training in Engineering Program Capstone

## Overview
A web application that displays opinions over time surrounding the police and law enforcement.

## Features
1. Opinions by location

2. Positive and negative word associations
The positive and negative associations of words within open text responses were analyzed using 
CloudNLP. The top positive and negative associations were displayed along with a wordcloud 
displaying all results (with size corresponding to prominence in the text and color corresponding
to average sentiment). You can view these results for the entirety of SF or a specific precinct.

3. Statistics and predictions

## Tools and Technologies
Some of the tools that will be used to build this portfolio are
* HTML
* CSS
* Javascript
* Java

## APIs
The following APIs were used
* Google Maps API
* Google Charts API
* CloudNLP (https://cloud.google.com/natural-language)
* Wordcloud2 (https://www.npmjs.com/package/wordcloud2)

## Deploying the project

To deploy the main project
1. Install maven (https://maven.apache.org/install.html)
2. Navigate to the capstone directory.
3. Modify pom.xml to contain the GCP project ID.
4. Ensure that GCP Cloud Build (https://console.developers.google.com/apis/api/cloudbuild.googleapis.com)
and natural language (https://console.developers.google.com/apis/library/language.googleapis.com) 
are enabled on the cloud project.
5. Run `mvn package appengine:deploy` to deploy the project live or 
`mvn package appengine:run` to run the project on the local server.

Additional instructions for deploying the python service:
TODO

## GitHub Checks
This repository runs checks on every pull request and commit. You can run these
locally from the root directory of the project.

- Java Continuous Integration: Run `mvn package`
- Java Format: Follow the instructions at
  https://github.com/google/google-java-format to download the .jar. Place the jar in the
  top level directory of the repository. You can then run it seperately with instructions 
  from the the formatter README (must use java 11) or run all tests together as shown below.
- JavaScript Lint:
  - One time setup: run `npm install`
  - Run `./node_modules/.bin/eslint capstone/src/.`
- All tests can be run using `./lint` or `./lint-replace` if you want to automatically fix
  the errors found

## License
This code is licensed under the Apache 2.0 License.
