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
/* global WordCloud */

let precinct = 'SF';

google.charts.load('current', {packages: ['corechart']});
google.charts.setOnLoadCallback(loadCharts);

function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map-container'),
      {center: {lat: 37.7749, lng: -122.4194},
        zoom: 12},
  );

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
  document.getElementById('map-key').style.display='none';
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
  zipcodeLayer.setStyle({fillColor: '#C698A0',
    fillOpacity: 0.9, visible: false});
  zipcodeLayer.addListener('click', function(event) {
    zipcodeLayer.revertStyle();
    zipcodeLayer.setStyle({fillColor: '#C698A0',
      fillOpacity: 0.9});
    zipcodeLayer.overrideStyle(event.feature, {
      fillColor: '#19B3B1', fillOpacity: .7});
  });

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
      zipcodeLayer.revertStyle();
      zipcodeLayer.setStyle({fillColor: '#C698A0',
        fillOpacity: 0.9, visible: true});
    } else {
      zipcodeLayer.setStyle({fillColor: '#C698A0',
        fillOpacity: 0.9, visible: false});
    }
  });
}

let precinctButtonOn= false;
function precinctControl(controlDiv, map) {
  //* *adding precinct overlay */
  const precinctLayer = new google.maps.Data({map: map});
  precinctLayer.loadGeoJson('policePrecincts.geojson');
  // a function that uses color map to map, checks if button is clicked
  precinctLayer.setStyle({fillColor: '#CECDBC',
    fillOpacity: 0.9, visible: false});
  mapAndSelection.map = precinctLayer;
  precinctLayer.addListener('click', function(event) {
    precinctLayer.revertStyle();
    precinctLayer.setStyle({fillColor: '#CECDBC',
      fillOpacity: 0.9});
    precinctLayer.overrideStyle(event.feature, {
      fillColor: '#19B3B1', fillOpacity: .7});
    precinct = event.feature.getProperty('district');
    document.getElementById('chart-title').textContent =
     precinct + ' Police Sentiment';
    loadCharts();
    associationUpdateDisplay(precinct);
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
      document.getElementById('map-key').style.display='none';
      precinctLayer.setStyle({visible: false});
      precinct = 'SF';
      document.getElementById('chart-title').textContent =
        'Sentiment Percentages in ' + precinct;
      loadCharts();
      associationUpdateDisplay('SF');
    } else {
      precinctLayer.setStyle({fillColor: '#CECDBC',
        fillOpacity: 0.9, visible: true});
      document.getElementById('map-key').style.display='block';
      mapAndSelection.selection = 'noneSelected';
    }
  });
}
function drawCheckboxLayer() {
  if (mapAndSelection.selection == 'noneSelected') {
    alert('nothing yet');
  } else if (mapAndSelection.selection == 'sentimentCheck') {
    const allPrecincts = ['Southern', 'Mission', 'Bayview',
      'Tenderloin', 'Central', 'Ingleside', 'Taraval', 'Park',
      'Northern', 'Richmond'];
    const allPrecinctColors = new Map();
    for (let i = 0; i < allPrecincts.length; i++) {
      allPrecinctColors.set(
          allPrecincts[i], averagePrecinctSentiment(allPrecincts[i]));
    }
    mapSentiment(allPrecinctColors);
  } else {
    alert('precinctLayer');
  }
}

async function associationUpdateDisplay(scope) {
  const response = await fetch('/associations?scope=' + scope);
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

window.addEventListener('load', createMap);
window.addEventListener('load', function() {
  associationUpdateDisplay('SF');
});
window.addEventListener('load', postSurveyResponses);

async function postSurveyResponses() {
  await fetch('/data', {method: 'POST'});
}

async function loadCharts() {
  const response = await fetch('/load-data?precinct=' + precinct);

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

  // Instantiate and draw the chart.
  const chart = new google.visualization.PieChart(
      document.getElementById('sentiment-pie-chart'));
  chart.draw(stats, null);
}

function loadResponseChart(totalResponses, precinct) {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', precinct);
  stats.addColumn('number', 'Total Responses');
  stats.addRows([
    [precinct, totalResponses],
  ]);
  // Instantiate and draw the chart.
  const chart = new google.visualization.BarChart(
      document.getElementById('response-bar-chart'));
  chart.draw(stats, null);
}

function mapSentiment(colorMap) {
  const precinctLayer = mapAndSelection.map;
  for (const [precinctName, precinctColor] of colorMap) {
    precinctLayer.revertStyle();
    precinctLayer.setStyle({fillColor: '#CECDBC',
      fillOpacity: 0.9});
    precinctLayer.overrideStyle(precinctLayer.getFeatureById(
        searchPrecinctsByDistrict(precinctName)),
    {fillColor: precinctColor, fillOpacity: 0.9});
  }
}

function searchPrecinctsByDistrict(desiredDistrict) {
  mapAndSelection.map.forEachFeature( function(feature) {
    if (feature.getProperty('district') == desiredDistrict) {
      return feature.getProperty('id');
    }
  });
}

async function averagePrecinctSentiment(policePrecinct) {
  const response = await fetch('/load-data?precinct=' + policePrecinct);
  const list = await response.json();
  let sentimentCount = 0;
  for (let i = 0; i < list.length; i++) {
    const sentiment = list[i].score;
    if (sentiment >= 0.5) {
      sentimentCount += 5;
    } else if (sentiment > 0.05) {
      sentimentCount += 4;
    } else if (sentiment >= -0.05) {
      sentimentCount += 3;
    } else if (sentiment > -0.5) {
      sentimentCount += 2;
    } else {
      sentimentCount += 1;
    }
  }
  const sentimentAverage = sentimentCount/list.length;
  return getSentimentColor(sentimentAverage);
}

function getSentimentColor(averageFeelings) {
  if (Math.round(averageFeelings) == 5) {
    return '#165B33';
  } else if (Math.round(averageFeelings) == 4) {
    return '#146B3A';
  } else if (Math.round(averageFeelings) == 3) {
    return '#F8B229';
  } else if (Math.round(averageFeelings) == 2) {
    return '#EA4630';
  } else {
    return '#BB2528';
  }
}

const mapAndSelection = {};
/* eslint-disable no-unused-vars */
function onlyOne(checkbox) {
  const checkboxes = document.getElementsByName('check');
  checkboxes.forEach((item) => {
    if (item !== checkbox) item.checked = false;
  });
  mapAndSelection.selection = checkbox.id;
  drawCheckboxLayer();
}
/* eslint-enable no-unused-vars */

const MAX_SIZE = 100;

function getColor(gradient) {
  if (gradient < 0) {
    const red = 255 * (Math.abs(gradient));
    return 'rgb(' + red + ', 0, 0)';
  } else {
    const green = 255 * gradient;
    return 'rgb(0, ' + green + ', 0)';
  }
}

async function loadWordcloud() {
  const response = await fetch('/wordcloud?scope=' + precinct);
  const data = await response.json();
  data.sort(function(a, b) {
    b.weight - a.weight;
  });
  data.map(function(x) {
    x.weight = Math.sqrt(x.weight);
  });
  const scalar = MAX_SIZE / data[0].weight;
  data.map(function(x) {
    x.weight = x.weight * scalar;
  });
  const list = data.map(function(x) {
    return [x.content, x.weight];
  });
  const color = function(word, weight, fontSize, distance, theta) {
    const elem = data.find(function(elem) {
      return elem.content === word;
    });
    return getColor(elem.gradient);
  };
  /* eslint-disable new-cap */
  WordCloud(document.getElementById('cloud-canvas'),
      {list: list, color: color} );
  /* eslint-enable new-cap */
}

function configModal() {
  //Get the map key
  const key = document.getElementById('map-key');

  //Get the space for word cloud
  const cloud = document.getElementById('map-cloud');

  // Get the modal
  const modal = document.getElementById('modal');

  // Get the button that opens the modal
  const btn = document.getElementById('map-cloud');

  // Get the <span> element that closes the modal
  const span = document.getElementById('modal-close');

  // When the user clicks the button, open the modal
  btn.onclick = function() {
    modal.style.display = 'block'; 
    key.style.display = 'none'; 
    cloud.style.display = 'none';  
    loadWordcloud();
  };

  // When the user clicks on <span> (x), close the modal
  span.onclick = function() {
    modal.style.display = 'none';
    key.style.display = 'block';
    cloud.style.display = 'block';
  };

  // When the user clicks anywhere outside of the modal, close it
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = 'none';
      key.style.display = 'block';
      cloud.style.display = 'block';
    }
  };
}

window.addEventListener('load', configModal);
