package com.google.sps.data;

import java.util.ArrayList;

/* This class stores data on various features in the model */
public class FeatureData {
  private static ArrayList<Precinct> precinctData;

  private final int NUMBER_OF_PRECINCTS = 10;

  /**
   * The precinct data was taken from public datasets such as
   * https://www.incomebyzipcode.com/california/94111 and
   * https://www.sanfranciscopolice.org/stay-safe/crime-data/crime-dashboard
   */
  static {
    precinctData = new ArrayList<Precinct>();

    Precinct southern =
        new Precinct.Builder("Southern")
            .population(40384)
            .averageHouseholdIncome(15372750)
            .crimeRate(calculateRate(72, 40384))
            .policeStationRating(300)
            .build();

    Precinct mission =
        new Precinct.Builder("Mission")
            .population(128223)
            .averageHouseholdIncome(16422767)
            .crimeRate(calculateRate(139, 128223))
            .policeStationRating(430)
            .build();

    Precinct bayview =
        new Precinct.Builder("Bayview")
            .population(50538)
            .averageHouseholdIncome(13877450)
            .crimeRate(calculateRate(115, 50538))
            .policeStationRating(430)
            .build();

    Precinct tenderloin =
        new Precinct.Builder("Tenderloin")
            .population(56322)
            .averageHouseholdIncome(13444100)
            .crimeRate(calculateRate(37, 56322))
            .policeStationRating(290)
            .build();

    Precinct central =
        new Precinct.Builder("Central")
            .population(56322)
            .averageHouseholdIncome(13444100)
            .crimeRate(calculateRate(132, 56322))
            .policeStationRating(330)
            .build();

    Precinct ingleside =
        new Precinct.Builder("Ingleside")
            .population(175634)
            .averageHouseholdIncome(15510167)
            .crimeRate(calculateRate(102, 175634))
            .policeStationRating(360)
            .build();

    Precinct taraval =
        new Precinct.Builder("Taraval")
            .population(171554)
            .averageHouseholdIncome(13696700)
            .crimeRate(calculateRate(89, 171554))
            .policeStationRating(420)
            .build();

    Precinct park =
        new Precinct.Builder("Park")
            .population(152902)
            .averageHouseholdIncome(17995520)
            .crimeRate(calculateRate(71, 152902))
            .policeStationRating(240)
            .build();

    Precinct northern =
        new Precinct.Builder("Northern")
            .population(117963)
            .averageHouseholdIncome(180944670)
            .crimeRate(calculateRate(199, 117963))
            .policeStationRating(260)
            .build();

    Precinct richmond =
        new Precinct.Builder("Richmond")
            .population(136904)
            .averageHouseholdIncome(14603000)
            .crimeRate(calculateRate(104, 136904))
            .policeStationRating(280)
            .build();

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
  public static int calculateRate(int numCrimes, int population) {
    final int PER_RESIDENTS = 1000;

    return (int) ((double) numCrimes / ((double) population / PER_RESIDENTS) * 100);
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
