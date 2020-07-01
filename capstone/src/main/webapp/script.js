function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map-container'),
        {center: {lat: 37.7749, lng: -122.4194}, zoom: 12}
    );
  
    //**adding zipcode overlay */
    map.data.loadGeoJson('zipcode-data.json');
    //**adding precinct overlay */
    map.data.loadGeoJson('neighborhoods.json');
    
    map.data.setStyle({visible: false});

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

async function postSurveyResponses(){
    let response = await fetch('/data', {method: "POST"});
}

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
    const chart = new google.visualization.PieChart(document.getElementById('sentiment-pie-chart'));
    chart.draw(stats, null);

    loadReponseChart();
}

function loadReponseChart() {
  const stats = new google.visualization.DataTable();

  stats.addColumn('string', 'Period');
  stats.addColumn('number', 'Number of Responses');

    // Instantiate and draw the chart.
    const chart = new google.visualization.BarChart(document.getElementById('response-bar-chart'));
    chart.draw(stats, null);
}

/* below are methods for the statistics page */

async function getSurveyResponses(){
    let response = await fetch('/load-data');
    let list = await response.json(); //list of entities from datastore
    let scores = new Array();
    let sentimentCount = [0,0,0,0,0]; // Very positive, positive, neutral, negative, and very negative
    let genderCount = [0,0,0]; // Male, female, and unknown
    let responseTimes = new Array();
    let dates = new Array();
    let ages = new Array();
    
    for (let i = 0; i < list.length; i++) {
        scores.push(list[i].score);
        responseTimes.push(list[i].responseTime);
        dates.push(list[i].date);
        ages.push(list[i].ageRange);

        const gender = list[i].gender;
        if (gender === 'M') {
            genderCount[0] += 1;
        } else if (gender === 'F') {
            genderCount[1] += 1;
        } else {
            genderCount[2] += 1;
        }

        const sentiment = list[i].score;
        if (sentiment >= 0.5) {
            sentimentCount[0] += 1;
        } else if (sentiment > 0.05) {
            sentimentCount[1] += 1;
        } else if (sentiment >= -0.05) {
            sentimentCount[2] += 1;
        } else if (sentiment > -0.5) {
            sentimentCount[3] += 1;
        } else { 
            sentimentCount[4] += 1;
        }
    }

    loadPieSentimentChart(sentimentCount);
    loadGenderBarChart(genderCount);
}

function loadPieSentimentChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Sentiment');
    stats.addColumn('number', 'Percentage');
    stats.addRows([
        ['Very Positive', list[0]],
        ['Positive', list[1]],
        ['Neutral', list[2]],
        ['Negative', list[3]],
        ['Very Negative', list[4]],
    ]);

    let options = {
        title: 'Sentiment Percentages',
        pieHole: 0.3,
    };

    // Instantiate and draw the chart.
    const chart = new google.visualization.PieChart(document.getElementById('donut-sentiment'));
    chart.draw(stats, options);
}

function loadGenderBarChart(list){
    let stats = new google.visualization.DataTable();
    
    stats.addColumn('string', 'Gender');
    stats.addColumn('number', 'Number');
    stats.addRows([
        ['Male', list[0]],
        ['Female', list[1]],
        ['Unknown', list[2]],
    ]);

    // Instantiate and draw the chart.
    const chart = new google.visualization.BarChart(document.getElementById('gender-bar-chart'));
    chart.draw(stats, null);

    loadReponseChart();
}

