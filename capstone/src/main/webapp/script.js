function createMap() {
    const map = new google.maps.Map(document.getElementById('map-container'),
    {
        center: {lat: 37.7749, lng: -122.4194},
        zoom: 12
    });

    const cityBorder = [
        {lat: 37.708305, lng: -122.502691},
        {lat: 37.708229, lng: -122.393322}
    ]

    const cityLimit = new google.maps.Polygon({
        paths: cityBorder,
        strokeColor: 'black',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: 'black',
        fillOpacity: 0.35
    });
  
  cityLimit.setMap(map);

  //**button for centering map*/
  let centerControlDiv = document.createElement("div");
  let centerControl = new CenterControl(centerControlDiv, map);
  centerControlDiv.index = 1;
  map.controls[google.maps.ControlPosition.LEFT_CENTER].push(centerControlDiv);

  //**button for zipcode data layer */
  let zipControlDiv = document.createElement("div");
  let zipControl = new ZipControl(zipControlDiv, map);
  zipControlDiv.index = 2;
  map.controls[google.maps.ControlPosition.LEFT_CENTER].push(zipControlDiv);

  //**button for zipcode data layer */
  let precinctControlDiv = document.createElement("div");
  let precinctControl = new PrecinctControl(precinctControlDiv, map);
  precinctControlDiv.index = 3;
  map.controls[google.maps.ControlPosition.LEFT_CENTER].push(precinctControlDiv);
}

function CenterControl(controlDiv, map) {
  //**button creation and positioning*/
  let controlUI = document.createElement("div");
  controlUI.style.backgroundColor = "#fff";
  controlUI.style.border = "2px solid #fff";
  controlUI.style.borderRadius = "3px";
  controlUI.style.boxShadow = "0 2px 6px rgba(0,0,0,.3)";
  controlUI.style.cursor = "pointer";
  controlUI.style.marginBottom = "22px";
  controlUI.style.textAlign = "center";
  controlUI.title = "Click to recenter the map";
  controlDiv.appendChild(controlUI);

  //**css for interior of all buttons*/
  let text = document.createElement("div");
  text.style.color = "rgb(25,25,25)";
  text.style.fontFamily = "Roboto,Arial,sans-serif";
  text.style.fontSize = "16px";
  text.style.lineHeight = "38px";
  text.style.paddingLeft = "5px";
  text.style.paddingRight = "5px";
  text.innerHTML = "Center Map";
  controlUI.appendChild(text);

  //**button functionality */
  controlUI.addEventListener("click", function() {
    map.setCenter({lat: 37.7749, lng: -122.4194});
  });
}

let zipClicks = 0;
function ZipControl(controlDiv, map) {
    //**adding zipcode overlay*/
    let zipcode_layer = new google.maps.Data({map: map});
    zipcode_layer.loadGeoJson('zipcode-data.json');
    zipcode_layer.setStyle({visible: false});

  //**button creation and positioning*/
  let controlUI = document.createElement("div");
  controlUI.style.backgroundColor = "#fff";
  controlUI.style.border = "2px solid #fff";
  controlUI.style.borderRadius = "3px";
  controlUI.style.boxShadow = "0 2px 6px rgba(0,0,0,.3)";
  controlUI.style.cursor = "pointer";
  controlUI.style.marginBottom = "22px";
  controlUI.style.textAlign = "center";
  controlUI.title = "Click to show San Fransisco zip codes";
  controlDiv.appendChild(controlUI);

  //**css for interior of all buttons*/
  let text = document.createElement("div");
  text.style.color = "rgb(25,25,25)";
  text.style.fontFamily = "Roboto,Arial,sans-serif";
  text.style.fontSize = "16px";
  text.style.lineHeight = "38px";
  text.style.paddingLeft = "5px";
  text.style.paddingRight = "5px";
  text.innerHTML = "Show Zipcodes";
  controlUI.appendChild(text);

  //**button functionality */
  controlUI.addEventListener("click", function() {
    zipClicks++;
    if(zipClicks%2==1){
        zipcode_layer.setStyle({visible: true});
    }
    else{
        zipcode_layer.setStyle({visible: false});
    }
  });
}

let precinctClicks = 0;
function PrecinctControl(controlDiv, map) {
    //**adding precinct overlay */
    let precinct_layer = new google.maps.Data({map: map});
    precinct_layer.loadGeoJson('neighborhoods.json');
    precinct_layer.setStyle({visible: false});

  //**button creation and positioning*/
  let dataUI = document.createElement("div");
  dataUI.style.backgroundColor = "#fff";
  dataUI.style.border = "2px solid #fff";
  dataUI.style.borderRadius = "3px";
  dataUI.style.boxShadow = "0 2px 6px rgba(0,0,0,.3)";
  dataUI.style.cursor = "pointer";
//   dataUI.style.marginBottom = "22px";
  dataUI.style.textAlign = "center";
  dataUI.title = "Click to show San Fransisco precincts";
  controlDiv.appendChild(dataUI);

  //**css for interior of all buttons*/
  let buttonText = document.createElement("div");
  buttonText.style.color = "rgb(25,25,25)";
  buttonText.style.fontFamily = "Roboto,Arial,sans-serif";
  buttonText.style.fontSize = "16px";
  buttonText.style.lineHeight = "38px";
  buttonText.style.paddingLeft = "5px";
  buttonText.style.paddingRight = "5px";
  buttonText.innerHTML = "Show Precincts";
  dataUI.appendChild(buttonText);

  //**button functionality */
  dataUI.addEventListener("click", function() {
    precinctClicks++;
    if(precinctClicks%2==0){
        precinct_layer.setStyle({visible: false});
    }
    else{
        precinct_layer.setStyle({visible: true});
    }
  });
}

window.addEventListener('load', createMap);

async function associationUpdateDisplay() {
  const response = await fetch('/associations');
  const associations = await response.json();
  
  const positive = document.getElementById('pos-associations');
  positive.innerHTML = '';
  associations.positive.forEach(elem => addListElement(positive, elem));

  const negative = document.getElementById('neg-associations');
  negative.innerHTML = '';
  associations.negative.forEach(elem => addListElement(negative, elem));
}

function addListElement(list, contents) {
  const elem = document.createElement('li');
  elem.textContent = contents;
  list.appendChild(elem);
}

window.addEventListener('load', associationUpdateDisplay);

function loadCharts(){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Sentiment');
    stats.addColumn('number', 'Percentage');
    stats.addRows([
        ['Positive', 0.7],
        ['Neutral', 0.1],
        ['Negative', 0.2],
    ]);

    // Instantiate and draw the chart.
    let chart = new google.visualization.PieChart(document.getElementById('sentiment-pie-chart'));
    chart.draw(stats, null);

    loadReponseChart();
}

function loadReponseChart(){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Period');
    stats.addColumn('number', 'Number of Responses');

    stats.addRows([
        ['Daily', 4],
        ['Weekly', 15]
    ]);

    // Instantiate and draw the chart.
    let chart = new google.visualization.BarChart(document.getElementById('response-bar-chart'));
    chart.draw(stats, null);
}
