function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map-container'),
      {center: {lat: 37.7749, lng: -122.4194}, zoom: 10});

	    //**adding zipcode overlay */
        map.data.loadGeoJson('zipcode-data.json');
        map.data.setStyle({visible: false});

    //**cordinates for shapes */
    var cityLimits = [
        {lat: 37.708305, lng: -122.502691},
        {lat: 37.708229, lng: -122.393322}
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
        {lat :37.722815, lng: -122.424100},
        {lat: 37.722860, lng: -122.426518},
        {lat: 37.724393, lng: -122.425343},
        {lat: 37.725659, lng: -122.421427},
        {lat: 37.724466, lng: -122.420893},
        {lat: 37.724703, lng: -122.419927},
        {lat: 37.723493, lng: -122.419458},
        {lat: 37.723781, lng: -122.418436},
        {lat: 37.722576, lng: -122.417942},
        {lat: 37.722821, lng: -122.416941},
        {lat: 37.721932, lng: -122.416552},

    ]

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
  var precinctTen = new google.maps.Polygon({
    paths: precinctTen,
    strokeColor: '#FFCF00',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FFFC00',
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
  precinctTen.setMap(map);
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

window.addEventListener('load', associationUpdateDisplay);
