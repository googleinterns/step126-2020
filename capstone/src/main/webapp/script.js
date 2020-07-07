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

function createMap() {
  const map = new google.maps.Map(document.getElementById('map-container'),
      {
        center: {lat: 37.7749, lng: -122.4194},
        zoom: 12,
      });

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
  controlUI.classList.add("button");
  controlUI.title = 'Click to recenter the map';
  controlDiv.appendChild(controlUI);

  //* *css for interior of all buttons*/
  const text = document.createElement('div');
  text.innerHTML = 'Center Map';
  controlUI.appendChild(text);

  //* *button functionality */
  controlUI.addEventListener('click', function() {
    map.setCenter({lat: 37.7749, lng: -122.4194});
  });
}

let zipClicks = 0;
function zipControl(controlDiv, map) {
  //* *adding zipcode overlay*/
  const zipcodeLayer = new google.maps.Data({map: map});
  zipcodeLayer.loadGeoJson('zipcode-data.json');
  zipcodeLayer.setStyle({visible: false});

  //* *button creation and positioning*/
  const controlUI = document.createElement('div');
  controlUI.classList.add("button");
  controlUI.title = 'Click to show San Fransisco zip codes';
  controlDiv.appendChild(controlUI);

  //* *css for interior of all buttons*/
  const text = document.createElement('div');
  text.innerHTML = 'Show Zipcodes';
  controlUI.appendChild(text);

  //* *button functionality */
  controlUI.addEventListener('click', function() {
    zipClicks++;
    if (zipClicks%2==1) {
      zipcodeLayer.setStyle({visible: true});
    } else {
      zipcodeLayer.setStyle({visible: false});
    }
  });
}

let precinctClicks = 0;
function precinctControl(controlDiv, map) {
  //* *adding precinct overlay */
  const precinctLayer = new google.maps.Data({map: map});
  precinctLayer.loadGeoJson('neighborhoods.json');
  precinctLayer.setStyle({visible: false});

  //* *button creation and positioning*/
  const dataUI = document.createElement('div');
  dataUI.classList.add("button");
  dataUI.title = 'Click to show San Fransisco precincts';
  controlDiv.appendChild(dataUI);

  //* *css for interior of all buttons*/
  const buttonText = document.createElement('div');
  buttonText.innerHTML = 'Show Precincts';
  dataUI.appendChild(buttonText);

  //* *button functionality */
  dataUI.addEventListener('click', function() {
    precinctClicks++;
    if (precinctClicks%2==0) {
      precinctLayer.setStyle({visible: false});
    } else {
      precinctLayer.setStyle({visible: true});
    }
  });
}

window.addEventListener('load', createMap);

async function associationUpdateDisplay() {
  const response = await fetch('/associations');
  const associations = await response.json();

  const positive = document.getElementById('pos-associations');
  positive.innerHTML = '';
  associations.positive.forEach((elem) => addListElement(positive, elem));

  const negative = document.getElementById('neg-associations');
  negative.innerHTML = '';
  associations.negative.forEach((elem) => addListElement(negative, elem));
}

function addListElement(list, contents) {
  const elem = document.createElement('li');
  elem.textContent = contents;
  list.appendChild(elem);
}

window.addEventListener('load', associationUpdateDisplay);

function loadCharts() {
  const stats = new google.visualization.DataTable();
  stats.addColumn('string', 'Sentiment');
  stats.addColumn('number', 'Percentage');
  stats.addRows([
    ['Positive', 0.7],
    ['Neutral', 0.1],
    ['Negative', 0.2],
  ]);

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('sentiment-pie-chart'));
  chart.draw(stats, null);

  loadReponseChart();
}

function loadReponseChart() {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Period');
  stats.addColumn('number', 'Number of Responses');

  stats.addRows([
    ['Daily', 4],
    ['Weekly', 15],
  ]);

  // Instantiate and draw the chart.
  const chart = new google.visualization.BarChart(
      document.getElementById('response-bar-chart'));
  chart.draw(stats, null);
}

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(loadCharts);
