package com.google.sps.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapData {

  static final Map<String, ArrayList> zipPrecinctMap;
  static final Map<String, ArrayList> precinctZipMap;

  /* Map that accounts for population density when mapping zipcodes to precincts.
  Ensures that any zip code only maps to one precinct */

  static final Map<String, String> populationZipPrecinct;
  static final Map<String, ArrayList> populationPrecinctZip;

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
    
    populationZipPrecinct = new HashMap<>();
    populationZipPrecinct.put("94103", "Southern" ));
    populationZipPrecinct.put("94107", "Southern"));
    populationZipPrecinct.put("94109", "Tenderloin"));
    populationZipPrecinct.put("94110", "Mission"));
    populationZipPrecinct.put("94112", "Taraval"));
    populationZipPrecinct.put("94114", "Mission"));
    populationZipPrecinct.put("94116", "Taraval"));
    populationZipPrecinct.put("94117", "Park"));
    populationZipPrecinct.put("94121", "Richmond"));
    populationZipPrecinct.put("94122", "Taraval"));
    populationZipPrecinct.put("94123", "Northern"));
    populationZipPrecinct.put("94124", "Bayview"));
    populationZipPrecinct.put("94129", "Richmond"));
    populationZipPrecinct.put("94131", "Ingleside"));
    populationZipPrecinct.put("94133", "Central"));

    populationPrecinctZip = new HashMap<>();
    populationPrecinctZip.put("Southern", new ArrayList<>(Arrays.asList("94103", "94107")));
    populationPrecinctZip.put("Mission", new ArrayList<>(Arrays.asList("94110", "94114")));
    populationPrecinctZip.put("Bayview", new ArrayList<>(Arrays.asList("94124")));
    populationPrecinctZip.put("Tenderloin", new ArrayList<>(Arrays.asList("94109")));
    populationPrecinctZip.put("Central", new ArrayList<>(Arrays.asList("94133")));
    populationPrecinctZip.put("Ingleside", new ArrayList<>(Arrays.asList("94131")));
    populationPrecinctZip.put("Taraval", new ArrayList<>(Arrays.asList("94112", "94116", "94122")));
    populationPrecinctZip.put(
        "Park", new ArrayList<>(Arrays.asList("94117", "94118")));
    populationPrecinctZip.put("Northern", new ArrayList<>(Arrays.asList("94123")));
    populationPrecinctZip.put(
        "Richmond", new ArrayList<>(Arrays.asList("94121", "94129")));
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

  /**
   * Gets the precinct that maps to a zip code
   *
   * @return String precinct that maps to a zip code
   */
  public static String getPopulationPrecinct(String zipCode) {
    return populationZipPrecinct.get(zipCode);
  }

   /**
   * Gets the list of zip codes that overlap with one precinct
   *
   * @return Arraylist list of zip codes that overlap with one precinct
   */
  public static ArrayList getPopulationZipCodes(String precinct) {
    return populationPrecinctZip.get(precinct);
  }
}
