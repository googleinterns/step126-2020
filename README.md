# Google Student Training in Engineering Program Capstone

## Overview
A web application that displays opinions over time surrounding the police and law enforcement.

## Features
1. Map with data layers

2. Positive and negative word associations

The positive and negative associations of words within open text responses were analyzed using 
CloudNLP. The top positive and negative associations were displayed along with a wordcloud 
displaying all results (with size corresponding to prominence in the text and color corresponding
to average sentiment). You can view these results for the entirety of SF or a specific precinct.

3. Statistics and predictions

Fetches from the Google Surveys API, transforms data for GCP Datastore, and visualizes analysis through
the Google Charts API. This feature also runs a decision tree regression model on the survey results to get predicted
sentiment scores for users based on their gender, age group, and whether they had a direct experience with the
police or not. The program for regression analysis was written in Python with the Flask framework. Using crom jobs and
a separate python service on GAE, new prediction results can be automatically generated for new survey responses.
There is a statistics page with survey-specific details such as response time and completion,
age and gender break down, and sentiment percentages for each precinct. 

## Tools and Technologies
Some of the tools that will be used to build this portfolio are
* HTML
* CSS
* Javascript
* Java
* Python

## APIs
The following APIs were used
* Google Maps API
* Google Charts API
* CloudNLP (https://cloud.google.com/natural-language)
* Wordcloud2 (https://www.npmjs.com/package/wordcloud2)
* SciKit-Learn
* Google Surveys API

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
1. Ensure that you have the requirement.txt file set up with 
all the relevant dependencies. Do pip freeze to get the dependencies
2. Set up a new app.yaml file with the service name, runtime, static files or
script specified
3. Keep these files along with your source code in a separate directory
4. Deploy the separate service using gcloud app deploy --project [PROJECT_ID]
5. (Optional) Include a cron.yaml file with the schedule field specified

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
