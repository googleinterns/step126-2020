package com.google.sps.data;

import java.util.ArrayList;

/* This class stores data on various features in the model */
public class FeatureData {
  private static ArrayList<Precinct> precinctData;

  private final int NUMBER_OF_PRECINCTS = 10;

  // Initialize the list of precincts
  static {
    precinctData = new ArrayList<Precinct>();

    Precinct southern = new Precinct();
    southern.setName("Southern");
    southern.setPopulation(40384);
    southern.setAverageHouseholdIncome(153727.5f);
    southern.setCrimeRate(calculateRate(72, 40384));
    southern.setPoliceStationRating(3.0f);

    Precinct mission = new Precinct();
    mission.setName("Mission");
    mission.setPopulation(128223);
    mission.setAverageHouseholdIncome(164227.67f);
    mission.setCrimeRate(calculateRate(139, 128223));
    mission.setPoliceStationRating(4.3f);

    Precinct bayview = new Precinct();
    bayview.setName("Bayview");
    bayview.setPopulation(50538);
    bayview.setAverageHouseholdIncome(138774.5f);
    bayview.setCrimeRate(calculateRate(115, 50538));
    bayview.setPoliceStationRating(4.3f);

    Precinct tenderloin = new Precinct();
    tenderloin.setName("Tenderloin");
    tenderloin.setPopulation(56322);
    tenderloin.setAverageHouseholdIncome(134441f);
    tenderloin.setCrimeRate(calculateRate(37, 56322));
    tenderloin.setPoliceStationRating(2.9f);

    Precinct central = new Precinct();
    central.setName("Central");
    central.setPopulation(56322);
    central.setAverageHouseholdIncome(134441f);
    central.setCrimeRate(calculateRate(132, 56322));
    central.setPoliceStationRating(3.3f);

    Precinct ingleside = new Precinct();
    ingleside.setName("Ingleside");
    ingleside.setPopulation(175634);
    ingleside.setAverageHouseholdIncome(155101.67f);
    ingleside.setCrimeRate(calculateRate(102, 175634));
    ingleside.setPoliceStationRating(3.6f);

    Precinct taraval = new Precinct();
    taraval.setName("Taraval");
    taraval.setPopulation(171554);
    taraval.setAverageHouseholdIncome(136967f);
    taraval.setCrimeRate(calculateRate(89, 171554));
    taraval.setPoliceStationRating(4.2f);

    Precinct park = new Precinct();
    park.setName("Park");
    park.setPopulation(152902);
    park.setAverageHouseholdIncome(179955.2f);
    park.setCrimeRate(calculateRate(71, 152902));
    park.setPoliceStationRating(2.4f);

    Precinct northern = new Precinct();
    northern.setName("Northern");
    northern.setPopulation(117963);
    northern.setAverageHouseholdIncome(180944.67f);
    northern.setCrimeRate(calculateRate(199, 117963));
    northern.setPoliceStationRating(2.6f);

    Precinct richmond = new Precinct();
    richmond.setName("Richmond");
    richmond.setPopulation(136904);
    richmond.setAverageHouseholdIncome(146030f);
    richmond.setCrimeRate(calculateRate(104, 136904));
    richmond.setPoliceStationRating(2.8f);

    precinctData.add(southern);
    precinctData.add(mission);
    precinctData.add(bayview);
    precinctData.add(tenderloin);
    precinctData.add(central);
    precinctData.add(ingleside);
    precinctData.add(taraval);
    precinctData.add(park);
    precinctData.add(richmond);
    precinctData.add(northern);
  }

  /**
   * Calculates the crime per 1,000 residents
   *
   * @param numCrimes Total reported crimes in a precinct for the current month
   * @param population Population of the precinct
   * @return float Crime rate for the precinct
   */
  public static float calculateRate(int numCrimes, int population) {
    final int PER_RESIDENTS = 1000;

    return (float) numCrimes / ((float) population / PER_RESIDENTS);
  }

  /**
   * Find and return a precinct by name
   *
   * @param name Name of the precinct
   * @return Precinct precinct with matching name
   */
  public static Precinct getPrecinct(String name) {
    for (Precinct p : precinctData) {
      String precinctName = p.getName();

      if (precinctName.equals(name)) {
        return p;
      }
    }

    return null;
  }
}
