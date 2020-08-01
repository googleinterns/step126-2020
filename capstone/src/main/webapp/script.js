// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* global google */
let precinct = 'SF';

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(loadCharts);

function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map-container'),
      {center: {lat: 37.7749, lng: -122.4194}, zoom: 12},
  );

  /* adding zipcode overlay */
  map.data.loadGeoJson('zipcode-data.json');
  /* adding precinct overlay */
  map.data.loadGeoJson('neighborhoods.json');

  map.data.setStyle({visible: false});

  const cityBorder = [
    {lat: 37.708305, lng: -122.502691},
    {lat: 37.708229, lng: -122.393322},
  ];

  const cityLimit = new google.maps.Polygon({
    paths: cityBorder,
    strokeColor: 'black',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: 'black',
    fillOpacity: 0.35,
  });

  cityLimit.setMap(map);

  //* *button for centering map*/
  const centerControlDiv = document.createElement('div');
  centerControl(centerControlDiv, map);
  map.controls[google.maps.ControlPosition.LEFT_CENTER].push(centerControlDiv);

  //* *button for zipcode data layer */
  const zipControlDiv = document.createElement('div');
  zipControl(zipControlDiv, map);
  map.controls[google.maps.ControlPosition.LEFT_CENTER].push(zipControlDiv);

  //* *button for zipcode data layer */
  const precinctControlDiv = document.createElement('div');
  precinctControl(precinctControlDiv, map);
  map.controls[google.maps.ControlPosition.LEFT_CENTER]
      .push(precinctControlDiv);
}

function centerControl(controlDiv, map) {
  //* *button creation and positioning*/
  const controlUI = document.createElement('div');
  controlUI.classList.add('button');
  controlUI.title = 'Click to recenter the map';
  controlDiv.appendChild(controlUI);

  //* *css for interior of all buttons*/
  const text = document.createElement('div');
  text.innerHTML = 'Center Map';
  controlUI.appendChild(text);

  //* *button functionality */
  controlUI.addEventListener('click', function() {
    map.setCenter({lat: 37.7749, lng: -122.4194});
    map.setZoom(12);
  });
}

let zipClicked = false;
function zipControl(controlDiv, map) {
  //* *adding zipcode overlay*/
  const zipcodeLayer = new google.maps.Data({map: map});
  zipcodeLayer.loadGeoJson('zipcode-data.json');
  zipcodeLayer.setStyle({visible: false});

  //* *button creation and positioning*/
  const controlUI = document.createElement('div');
  controlUI.classList.add('button');
  controlUI.title = 'Click to show San Fransisco zip codes';
  controlDiv.appendChild(controlUI);

  //* *css for interior of all buttons*/
  const text = document.createElement('div');
  text.innerHTML = 'Show Zipcodes';
  controlUI.appendChild(text);

  //* *button functionality */
  controlUI.addEventListener('click', function() {
    zipClicked = !zipClicked;
    if (zipClicked) {
      zipcodeLayer.setStyle({visible: true});
    } else {
      zipcodeLayer.setStyle({visible: false});
    }
  });
}

let precinctButtonOn= false;
function precinctControl(controlDiv, map) {
  //* *adding precinct overlay */
  const precinctLayer = new google.maps.Data({map: map});
  precinctLayer.loadGeoJson('neighborhoods.json');
  precinctLayer.setStyle({visible: false});
  precinctLayer.addListener('click', function(event) {
    precinct = event.feature.getProperty('station');
    loadCharts();
  });
  //* *button creation and positioning*/
  const dataUI = document.createElement('div');
  dataUI.classList.add('button');
  dataUI.title = 'Click to show San Fransisco precincts';
  controlDiv.appendChild(dataUI);

  //* *css for interior of all buttons*/
  const buttonText = document.createElement('div');
  buttonText.innerHTML = 'Show Precincts';
  dataUI.appendChild(buttonText);

  //* *button functionality */
  dataUI.addEventListener('click', function() {
    precinctButtonOn = !precinctButtonOn;
    if (!precinctButtonOn) {
      precinctLayer.setStyle({visible: false});
      precinct = 'SF';
      loadCharts();
    } else {
      precinctLayer.setStyle({visible: true});
    }
  });
}

window.addEventListener('load', createMap);
window.addEventListener('load', postSurveyResponses);

async function postSurveyResponses() {
  await fetch('/data', {method: 'POST'});
}

async function loadCharts() {
  const response = await fetch('/load-data?kind=Response&precinct=' + precinct);

  sessionStorage.setItem('precinct', precinct);

  const list = await response.json(); // list of entities from datastore
  const size = list.length;

  const sentimentCount = {
    'Positive': 0,
    'Neutral': 0,
    'Negative': 0,
  };

  for (let i = 0; i < size; i++) {
    // Summarize the sentiment score (-1 to 1)
    const sentiment = list[i].score;
    if (sentiment > 0.05) {
      sentimentCount['Positive'] += 1;
    } else if (sentiment >= -0.05) {
      sentimentCount['Neutral'] += 1;
    } else {
      sentimentCount['Negative'] += 1;
    }
  }

  loadSentimentPieChart(sentimentCount);
  loadResponseChart(size, precinct);
}

function loadSentimentPieChart(sentimentCount) {
  const stats = new google.visualization.DataTable();
  stats.addColumn('string', 'Sentiment');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Positive', sentimentCount['Positive']],
    ['Neutral', sentimentCount['Neutral']],
    ['Negative', sentimentCount['Negative']],
  ]);

  const options = {
    animation: {
      startup: true,
      duration: 1000,
      easing: 'out',
    },
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('sentiment-pie-chart'));
  chart.draw(stats, options);
}

function loadResponseChart(totalResponses, precinct) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', precinct);
  stats.addColumn('number', 'Total Responses');
  stats.addRows([
    [precinct, totalResponses],
  ]);

  const options = {
    animation: {
      startup: true,
      duration: 3000,
      easing: 'out',
    },
  };

  // Instantiate and draw the chart.
  const chart = new google.visualization.BarChart(
      document.getElementById('response-bar-chart'));
  chart.draw(stats, options);
}

function showStats() {
  window.location.href = 'statistics.html';
}
