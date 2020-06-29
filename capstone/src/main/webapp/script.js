function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map-container'),
      {center: {lat: 37.7749, lng: -122.4194}, zoom: 12});

	    //**adding zipcode overlay */
        map.data.loadGeoJson('zipcode-data.json');
        map.data.setStyle({visible: false});

    //**cordinates for shapes */
    var cityLimits = [
        {lat: 37.708305, lng: -122.502691},
        {lat: 37.708229, lng: -122.393322}
    ]

    var precinctEleven = [
        {lat: 37.724393, lng: -122.425343},
        {lat: 37.725659, lng: -122.421427},
        {lat: 37.724466, lng: -122.420893},
        {lat: 37.724703, lng: -122.419927},
        {lat: 37.723493, lng: -122.419458},
        {lat: 37.723781, lng: -122.418436},
        {lat: 37.722576, lng: -122.417942},
        {lat: 37.722821, lng: -122.416941},
        {lat: 37.721932, lng: -122.416552},
        {lat: 37.720022, lng:-122.414610},
        {lat: 37.718043, lng: -122.414166},
        {lat: 37.718925, lng: -122.419941},
        {lat: 37.717123, lng: -122.424374},
        {lat: 37.718592, lng: -122.426423},
        {lat: 37.715007, lng: -122.426996},
        {lat: 37.714219, lng: -122.425830},
        {lat: 37.711473, lng: -122.427400},
        {lat: 37.712253, lng: -122.429527},
        {lat: 37.710645, lng: -122.431434},
        {lat: 37.709217, lng: -122.4284},
        {lat: 37.708685, lng: -122.428169},
        {lat: 37.708261, lng: -122.428410},
        {lat: 37.708415, lng: -122.469266},
        {lat: 37.711267, lng: -122.462738},
        {lat:37.712727, lng: -122.470941},
        {lat: 37.719708, lng: -122.472445},
        {lat: 37.720048, lng: -122.462697},
        {lat: 37.720048, lng: -122.462697},
        {lat: 37.723285, lng: -122.453190},
        {lat: 37.723111, lng: -122.447711},
        {lat: 37.729496, lng: -122.442412},
        {lat: 37.731774, lng: -122.434805},
        {lat: 37.731978, lng: -122.421318},
        {lat: 37.729016, lng: -122.419892},
        {lat: 37.728725, lng: -122.423536}

    ]

    var precinctTen = [
        {lat: 37.708263, lng: -122.428442},
        {lat: 37.708702, lng: -122.428171},
        {lat: 37.709288, lng: -122.428418},
        {lat: 37.709606, lng: -122.429271},
        {lat: 37.710596, lng: -122.431476},
        {lat: 37.712300, lng: -122.429680},
        {lat: 37.711408, lng: -122.427353},
        {lat: 37.714243, lng: -122.425809},
        {lat: 37.714991, lng: -122.426999},
        {lat: 37.718564, lng: -122.426440},
        {lat: 37.717077, lng: -122.424649},
        {lat: 37.718979, lng: -122.420104},
        {lat: 37.718040, lng: -122.414321},
        {lat: 37.721717, lng: -122.400485},
        {lat: 37.738258, lng: -122.408128},
        {lat: 37.769080, lng: -122.408275},
        {lat: 37.771836, lng: -122.401828},
        {lat: 37.770490, lng: -122.400050},
        {lat: 37.776670, lng: -122.389031},
        {lat: 37.728694, lng: -122.356974},
        {lat: 37.716326, lng: -122.365777},
        {lat: 37.708643, lng: -122.374460}
    ];

    var precinctNine = [
        {lat: 37.718009, lng: -122.414431},
        {lat: 37.721729, lng: -122.400479},
        {lat: 37.738321, lng: -122.408232},
        {lat: 37.765959, lng: -122.408102},
        {lat: 37.765091, lng: -122.422886},
        {lat: 37.748021, lng: -122.422540},
        {lat: 37.737624, lng: -122.425339},
        {lat: 37.731719, lng: -122.435269},
        {lat: 37.732016, lng: -122.421451},
        {lat: 37.729011, lng: -122.419875},
        {lat: 37.728731, lng: -122.423613},
        {lat: 37.724444, lng: -122.425323},
        {lat: 37.725624, lng: -122.421419},
        {lat: 37.724438, lng: -122.420921},
        {lat: 37.724692, lng: -122.419935},
        {lat: 37.723524, lng: -122.419434},
        {lat: 37.723762, lng: -122.418407},
        {lat: 37.722588, lng: -122.417908},
        {lat: 37.722809, lng: -122.416975},
        {lat: 37.721861, lng: -122.416546},
        {lat: 37.719947, lng: -122.414504}
    ];

    var precinctEight = [
        {lat: 37.731765, lng: -122.434810},
        {lat: 37.731630, lng: -122.442059},
        {lat: 37.735412, lng: -122.441989},
        {lat: 37.745703, lng: -122.451726},
        {lat: 37.758982, lng: -122.448584},
        {lat: 37.758664, lng: -122.451429},
        {lat: 37.761446, lng: -122.452140},
        {lat:37.761790, lng: -122.446842},
        {lat: 37.767584, lng: -122.443981},
        {lat: 37.765990, lng: -122.442307},
        {lat: 37.769077, lng: -122.438528},
        {lat: 37.769103, lng: -122.422355},
        {lat: 37.747991, lng: -122.422512},
        {lat: 37.737651, lng: -122.425347}
    ];

    var precinctSeven = [
        {lat: 37.737651, lng: -122.425347}
    ];

  var precinct6 = [
    {lat: 37.826890, lng: -122.379144},
    {lat: 37.830382, lng: -122.377603},
    {lat: 37.832199, lng: -122.373619},
    {lat: 37.831105, lng: -122.368561},
    {lat: 37.822613, lng: -122.363079},
    {lat: 37.818900, lng: -122.364530},
    {lat: 37.816113, lng: -122.370882},
    {lat: 37.812734, lng: -122.369027},
    {lat: 37.812327, lng: -122.366881},
    {lat: 37.813649, lng: -122.364821},
    {lat: 37.814903, lng: -122.359200},
    {lat: 37.814293, lng: -122.358856},
    {lat: 37.812462, lng: -122.361131},
    {lat: 37.808258, lng: -122.361217},
    {lat: 37.806896, lng: -122.362465},
    {lat: 37.807405, lng: -122.367401},
    {lat: 37.810965, lng: -122.372851},
    {lat: 37.814598, lng: -122.371302},
    {lat: 37.826897, lng: -122.379137}
  ];

  var precinctSix = [
    {lat: 37.793779, lng: -122.392673},
    {lat: 37.790398, lng: -122.388557},
    {lat: 37.790492, lng: -122.384992},
    {lat: 37.779179, lng: -122.384883},
    {lat: 37.770441, lng: -122.400091},
    {lat: 37.771775, lng: -122.401804},
    {lat: 37.769055, lng: -122.408311},
    {lat: 37.765934, lng: -122.408140},
    {lat: 37.765120, lng: -122.422551},
    {lat: 37.769055, lng: -122.422381},
    {lat: 37.775004, lng: -122.419242},
    {lat: 37.785672, lng: -122.421428},
    {lat: 37.787355, lng: -122.408318},
    {lat: 37.784468, lng: -122.407637},
    {lat: 37.784061, lng: -122.408281},
    {lat: 37.782876, lng: -122.406493}
  ];

  var precinctThree = [
    {lat: 37.793723, lng: -122.392156},
    {lat: 37.804667, lng: -122.402254},
    {lat: 37.808092, lng: -122.408730},
    {lat: 37.808514, lng: -122.421306},
    {lat: 37.801774, lng: -122.412559},
    {lat: 37.800146, lng: -122.412473},
    {lat: 37.798490, lng: -122.423753},
    {lat: 37.785666, lng: -122.421475},
    {lat: 37.787377, lng: -122.408272},
    {lat: 37.784577, lng: -122.407559},
    {lat: 37.783999, lng: -122.408385},
	{lat: 37.782885, lng: -122.406537}
  ];

  /**drawing precincts */
 var cityLimits = new google.maps.Polygon({
    paths: cityLimits,
    strokeColor: 'black',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: 'black',
    fillOpacity: 0.35
  });
   var precinctEleven = new google.maps.Polygon({
    paths: precinctEleven,
    strokeColor: '#00FF00',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#00FF00',
    fillOpacity: 0.35
  });
  var precinctTen = new google.maps.Polygon({
    paths: precinctTen,
    strokeColor: '#FFCF00',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FFFC00',
    fillOpacity: 0.35
  });
  var precinctNine = new google.maps.Polygon({
    paths: precinctNine,
    strokeColor: '#00FFFF',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#00FFFF',
    fillOpacity: 0.35
  });
  var precinctEight = new google.maps.Polygon({
    paths: precinctEight,
    strokeColor: '#FF00FF',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF00FF',
    fillOpacity: 0.35
  });
  var precinctSeven = new google.maps.Polygon({
    paths: precinctSeven,
    strokeColor: '#FDFBA4',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#DFBA4F',
    fillOpacity: 0.35
  });
  var precinctIsland = new google.maps.Polygon({
    paths: precinct6,
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35
  });
  var precinctSix = new google.maps.Polygon({
    paths: precinctSix,
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35
  });
  var precinctThree = new google.maps.Polygon({
    paths: precinctThree,
    strokeColor: '#FFC000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FFC000',
    fillOpacity: 0.35
  });
  cityLimits.setMap(map);
  precinctThree.setMap(map);
  precinctSix.setMap(map);
  precinctIsland.setMap(map);
  precinctSeven.setMap(map);
  precinctEight.setMap(map);
  precinctNine.setMap(map);
  precinctTen.setMap(map);
  precinctEleven.setMap(map);
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
