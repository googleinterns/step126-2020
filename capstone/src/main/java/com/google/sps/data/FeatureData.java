package com.google.sps.data;

import com.google.sps.data.Precinct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* This class stores data on various features in the model */
public class FeatureData {
  private ArrayList<Precinct> precinctData;

  private final int NUMBER_OF_PRECINCTS = 10;

  // Initialize the list of precincts
  static {
    precinctData = new ArrayList<Precinct>();

    Precinct southern = new Precinct();
    southern.setName("Southern");
    southern.setPopulation(40384);
    southern.setAverageHouseholdIncome(153727.5);
    southern.setCrimeRate(calculateRate(72, 40384));
    southern.setPoliceStationRating(3.0);

    Precinct mission = new Precinct();
    southern.setName("Mission");
    southern.setPopulation(128223);
    southern.setAverageHouseholdIncome(164227.67);
    southern.setCrimeRate(calculateRate(139, 128223));
    southern.setPoliceStationRating(4.3);

    Precinct bayview = new Precinct();
    southern.setName("Bayview");
    southern.setPopulation(50538);
    southern.setAverageHouseholdIncome(138774.5);
    southern.setCrimeRate(calculateRate(115, 50538));
    southern.setPoliceStationRating(4.3);

    Precinct tenderloin = new Precinct();
    southern.setName("Tenderloin");
    southern.setPopulation(56322);
    southern.setAverageHouseholdIncome(134441);
    southern.setCrimeRate(calculateRate(37, 56322));
    southern.setPoliceStationRating(2.9);

    Precinct central = new Precinct();
    southern.setName("Central");
    southern.setPopulation(56322);
    southern.setAverageHouseholdIncome(134441);
    southern.setCrimeRate(calculateRate(132, 56322));
    southern.setPoliceStationRating(3.3);

    Precinct ingleside = new Precinct();
    southern.setName("Ingleside");
    southern.setPopulation(175634);
    southern.setAverageHouseholdIncome(155101.67);
    southern.setCrimeRate(calculateRate(102, 175634));
    southern.setPoliceStationRating(3.6);

    Precinct taraval = new Precinct();
    southern.setName("Taraval");
    southern.setPopulation(171554);
    southern.setAverageHouseholdIncome(136967);
    southern.setCrimeRate(calculateRate(89, 171554));
    southern.setPoliceStationRating(4.2);

    Precinct park = new Precinct();
    southern.setName("Park");
    southern.setPopulation(152902);
    southern.setAverageHouseholdIncome(179955.2);
    southern.setCrimeRate(calculateRate(71, 152902));
    southern.setPoliceStationRating(2.4);

    Precinct northern = new Precinct();
    southern.setName("Northern");
    southern.setPopulation(117963);
    southern.setAverageHouseholdIncome(180944.67);
    southern.setCrimeRate(calculateRate(199, 117963));
    southern.setPoliceStationRating(2.6);

    Precinct richmond = new Precinct();
    southern.setName("Richmond");
    southern.setPopulation(136904);
    southern.setAverageHouseholdIncome(146030);
    southern.setCrimeRate(calculateRate(104, 136904));
    southern.setPoliceStationRating(2.8);
  } 
  
   /**
   * Calculates the crime per 1,000 residents
   *
   * @param numCrimes Total reported crimes in a  
   * precinct for the current month
   * @param population Population of the precinct
   * @return float Crime rate for the precinct
   */
  public float calculateRate(int numCrimes, int population) {
    final int PER_RESIDENTS = 1000;

    return numCrimes / (population / PER_RESIDENTS);
  } 
}