function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map-container'),
      {center: {lat: 37.422, lng: -122.084}, zoom: 16});

    //**cordinates for shapes */
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

  /**drawing precinct 6 */
  var precincts = new google.maps.Polygon({
    paths: precinct6,
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35
  });
  precincts.setMap(map);
}
