package com.google.sps.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapData {

  static final Map<String, ArrayList> zipPrecinctMap;
  static final Map<String, ArrayList> precinctZipMap;

  static {
    zipPrecinctMap = new HashMap<>();
    zipPrecinctMap.put("94103", new ArrayList<>(Arrays.asList("Southern", "Mission")));
    zipPrecinctMap.put("94107", new ArrayList<>(Arrays.asList("Bayview", "Southern")));
    zipPrecinctMap.put(
        "94109", new ArrayList<>(Arrays.asList("Tenderloin", "Central", "Northern")));
    zipPrecinctMap.put("94110", new ArrayList<>(Arrays.asList("Ingleside", "Mission")));
    zipPrecinctMap.put("94112", new ArrayList<>(Arrays.asList("Ingleside", "Taraval")));
    zipPrecinctMap.put("94114", new ArrayList<>(Arrays.asList("Park", "Mission")));
    zipPrecinctMap.put("94116", new ArrayList<>(Arrays.asList("Taraval")));
    zipPrecinctMap.put("94117", new ArrayList<>(Arrays.asList("Park", "Northern")));
    zipPrecinctMap.put("94118", new ArrayList<>(Arrays.asList("Richmond", "Park")));
    zipPrecinctMap.put("94121", new ArrayList<>(Arrays.asList("Richmond")));
    zipPrecinctMap.put("94122", new ArrayList<>(Arrays.asList("Richmond", "Taraval", "Park")));
    zipPrecinctMap.put("94123", new ArrayList<>(Arrays.asList("Northern")));
    zipPrecinctMap.put("94124", new ArrayList<>(Arrays.asList("Bayview")));
    zipPrecinctMap.put("94129", new ArrayList<>(Arrays.asList("Richmond")));
    zipPrecinctMap.put("94131", new ArrayList<>(Arrays.asList("Ingleside", "Park")));

    precinctZipMap = new HashMap<>();
    precinctZipMap.put("Southern", new ArrayList<>(Arrays.asList("94103")));
    precinctZipMap.put("Mission", new ArrayList<>(Arrays.asList("94110", "94114", "94103")));
    precinctZipMap.put("Bayview", new ArrayList<>(Arrays.asList("94124", "94107")));
    precinctZipMap.put("Tenderloin", new ArrayList<>(Arrays.asList("94109")));
    precinctZipMap.put("Central", new ArrayList<>(Arrays.asList("94109")));
    precinctZipMap.put("Ingleside", new ArrayList<>(Arrays.asList("94110", "94131", "94112")));
    precinctZipMap.put("Taraval", new ArrayList<>(Arrays.asList("94112", "94116", "94122")));
    precinctZipMap.put(
        "Park", new ArrayList<>(Arrays.asList("94114", "94117", "94118", "94122", "94131")));
    precinctZipMap.put("Northern", new ArrayList<>(Arrays.asList("94109", "94117", "94123")));
    precinctZipMap.put(
        "Richmond", new ArrayList<>(Arrays.asList("94118", "94121", "94122", "94129")));
  }

  /**
   * Gets the precincts that overlap with one zip code region
   *
   * @return Arraylist list of precincts that overlap with zip code
   */
  public static ArrayList getPrecincts(String zipcode) {
    return zipPrecinctMap.get(zipcode);
  }

  /**
   * Gets the list of zip codes that overlap with one precinct
   *
   * @return Arraylist list of zip codes that overlap with one precinct
   */
  public static ArrayList getZipCodes(String precinct) {
    return precinctZipMap.get(precinct);
  }

  public ArrayList<String> getAllScopes() {
    return new ArrayList<String>(
        Arrays.asList(
            "SF",
            "Southern",
            "Mission",
            "Bayview",
            "Tenderloin",
            "Central",
            "Northern",
            "Ingleside",
            "Taraval",
            "Park",
            "Richmond"));
  }

  public ArrayList<String> getScope(String zipcode) {
    ArrayList<String> result = (ArrayList<String>) getPrecincts(zipcode).clone();
    result.add("SF");
    return result;
  }
}

