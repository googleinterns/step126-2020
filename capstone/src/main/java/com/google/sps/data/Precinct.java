package com.google.sps.data;

/* This class stores information pertaining to each precinct */
public class Precinct {
  
  /* Builder to construct each precinct object */
  public static class Builder {

    private String name;
    private int population;
    private float averageHouseholdIncome;
    private float crimeRate;
    private float policeStationRating;

    public Builder(String name) {
      this.name = name;
    }

    /**
     * Sets the population of the precinct
     *
     * @param population The number of people in the precinct
     * @return Builder instance
     */
    public Builder population(int population) {
      this.population = population;

      return this;
    }

    /**
     * Sets the household income average (USD) for the precinct
     *
     * @param averageHouseholdIncome Income in each household
     * @return Builder instance
     */
    public Builder averageHouseholdIncome(float averageHouseholdIncome) {
      this.averageHouseholdIncome = averageHouseholdIncome;

      return this;
    }

    /**
     * Sets the crime rate for the precinct
     *
     * @param crimeRate Crime rate per 1,000 residents
     * @return Builder instance
     */
    public Builder crimeRate(float crimeRate) {
      this.crimeRate = crimeRate;

      return this;
    }

    /**
     * Sets the rating for the police department in each precinct
     *
     * @param policeStationRating 1-5 star rating of the station
     * @return Builder instance
     */
    public Builder policeStationRating(float policeStationRating) {
      this.policeStationRating = policeStationRating;

      return this;
    }
    
    /**
     * Instantiates precinct and injects builder
     *
     * @return Precinct instance
     */
    public Precinct build() {
      Precinct precinct = new Precinct(this);

      return precinct;
    }
  }

  private String name;
  private int population;
  private float averageHouseholdIncome;
  private float crimeRate;
  private float policeStationRating;

  private Precinct(Builder builder) {
    this.name = builder.name;
    this.population = builder.population;
    this.averageHouseholdIncome = builder.averageHouseholdIncome;
    this.crimeRate = builder.crimeRate;
    this.policeStationRating = builder.policeStationRating;
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
   * Gets the average household income of the precinct
   *
   * @return float The average household income
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
