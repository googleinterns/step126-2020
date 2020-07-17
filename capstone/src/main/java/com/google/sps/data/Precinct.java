package com.google.sps.data;

/* This class stores information pertaining to each precinct */
public class Precinct {
  private String name;
  private int population;
  private float averageHouseholdIncome;
  private float crimeRate;
  private float policeStationRating;

  /**
   * Sets the name of the precinct
   *
   * @param name The precinct's name
   * @return Void
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the population of the precinct
   *
   * @param population The number of people in the precinct
   * @return Void
   */
  public void setPopulation(int population) {
    this.population = population;
  }

  /**
   * Sets the house hold income average (USD) for the precinct
   *
   * @param averageHouseholdIncome Income in each house hold
   * @return Void
   */
  public void setAverageHouseholdIncome(float averageHouseholdIncome) {
    this.averageHouseholdIncome = averageHouseholdIncome;
  }

  /**
   * Sets the crime rate for the precinct
   *
   * @param crimeRate Crime rate per 1,000 residents
   * @return Void
   */
  public void setCrimeRate(float crimeRate) {
    this.crimeRate = crimeRate;
  }

  /**
   * Sets the rating for the police department in each precinct
   *
   * @param policeStationRating 1-5 star rating of the station
   * @return Void
   */
  public void setPoliceStationRating(float policeStationRating) {
    this.policeStationRating = policeStationRating;
  }

  /**
   * Gets the name of the precinct
   *
   * @return String The name of the precinct
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the population of the precinct
   *
   * @return int The number of people
   */
  public int getPopulation() {
    return population;
  }

  /**
   * Gets the average house hold income of the precinct
   *
   * @return float The average house hold income
   */
  public float getAverageHouseholdIncome() {
    return averageHouseholdIncome;
  }

  /**
   * Gets the crime rate of the precinct
   *
   * @return float The number of crimes per 1,000 residents
   */
  public float getCrimeRate() {
    return crimeRate;
  }

  /**
   * Gets the police station rating of the precinct
   *
   * @return float The google review rating of the station
   */
  public float getPoliceStationRating() {
    return policeStationRating;
  }
}
