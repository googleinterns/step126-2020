function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map-container'),
        {center: {lat: 37.7749, lng: -122.4194}, zoom: 12}
    );
  
    //**adding zipcode overlay */
    map.data.loadGeoJson('zipcode-data.json');
    map.data.setStyle({visible: false});
    //**adding precinct overlay */
    map.data.loadGeoJson('neighborhoods.json');
    map.data.setStyle({visible: false});

    const cityBorder = [
        {lat: 37.708305, lng: -122.502691},
        {lat: 37.708229, lng: -122.393322}
    ]

    let cityLimit = new google.maps.Polygon({
        paths: cityBorder,
        strokeColor: 'black',
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: 'black',
        fillOpacity: 0.35
    });
  
  cityLimit.setMap(map);
}

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

window.addEventListener('load', associationUpdateDisplay)

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
