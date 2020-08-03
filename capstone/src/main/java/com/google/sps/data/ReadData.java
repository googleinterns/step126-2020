package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadData {
  private SentimentData sentimentService;
  private final String DELIMITER = ",";

  /**
   * Default constructor with no sentiment analysis dependency. Objects initialized through this
   * constructor can be locally tested since sentiment analysis is not authorized for local testing
   * at this time
   */
  public ReadData() throws IOException {
    sentimentService = null;
  }

  /**
   * Injected dependency included in this overloaded constuctor. Objects initialized through this
   * constructor will use sentiment analysis to analyze positivity/ negativity of survey responses.
   */
  public ReadData(SentimentData sentimentService) throws IOException {
    this.sentimentService = sentimentService;
  }

  /**
   * Calls read file while iterating through all files in the assets directory
   *
   * @return ArrayList<Entity> all the entities that were created from parsing every file in the
   *     assets directory
   */
  public ArrayList<Entity> allEntitiesFromFiles() throws IOException {
    ArrayList<Entity> allEntities = new ArrayList<Entity>();

    File folder = new File("assets/");

    for (File file : folder.listFiles()) {
      entitiesFromFile(allEntities, file);
    }

    if (sentimentService != null) {
      sentimentService.close();
    }

    return allEntities;
  }

  /**
   * Reads the survey csv, splits the lines by a comma delimiter, and creates entites with the
   * parsed data for datastore
   *
   * @param file This is the file that is being parsed
   * @param newEntities stores created entities
   * @return void
   */
  public void entitiesFromFile(ArrayList<Entity> newEntities, File file) {
    String zipCode = file.getName().substring(0, 5);
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException e) {
      return;
    }

    BufferedReader reader = null;

    final int EXPECTED_LENGTH = 15;

    try {
      reader = new BufferedReader(fileReader);
      String header = reader.readLine();
      String line = "";

      while ((line = reader.readLine()) != null) {
        String[] values = line.split(DELIMITER);
        String id = values[0];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;

        try {
          date = formatter.parse(values[1]);
        } catch (ParseException e) {
          date = null;
        }

        String completionStatus = values[2];
        String gender = values[4];
        String ageRange = values[5];
        String directExperience = values[8];
        String rating = values[9];
        String text = "";

        final int START_INDEX = 11;

        int indexOfInt = (values.length - EXPECTED_LENGTH) + START_INDEX + 1;

        for (int i = START_INDEX; i < indexOfInt; i++) {
          text += values[i];
        }

        float score = 0;

        if (sentimentService != null && text.length() > 0) {
          score = sentimentService.getSentiment(text);
        }

        int responseTimeOne = (int) Math.round(Double.parseDouble(values[indexOfInt++]));
        int responseTimeTwo = (int) Math.round(Double.parseDouble(values[indexOfInt++]));
        int responseTimeThree = (int) Math.round(Double.parseDouble(values[indexOfInt]));

        Entity entity = new Entity("Response", id);

        entity.setProperty("zipCode", zipCode);
        entity.setProperty("id", id);
        entity.setProperty("date", date);
        entity.setProperty("completionStatus", completionStatus);
        entity.setProperty("gender", gender);
        entity.setProperty("ageRange", ageRange);
        entity.setProperty("directExperience", directExperience);
        entity.setProperty("rating", rating);
        entity.setProperty("text", text);
        entity.setProperty("score", score);
        entity.setProperty("responseTimeOne", responseTimeOne);
        entity.setProperty("responseTimeTwo", responseTimeTwo);
        entity.setProperty("responseTimeThree", responseTimeThree);

        // If the survey maps to more than 1 precinct, average the precinct data
        ArrayList<String> precinctNames = MapData.getPrecincts(zipCode);

        int sumIncome = 0;
        int sumCrimeRate = 0;
        int sumStationRating = 0;
        int total = 0;
        for (String precinctName : precinctNames) {
          Precinct precinct = FeatureData.getPrecinct(precinctName);

          sumIncome += precinct.getAverageHouseholdIncome();
          sumCrimeRate += precinct.getCrimeRate();
          sumStationRating += precinct.getPoliceStationRating();

          total++;
        }

        // Store these properties in the hundreds to avoid precision errors
        if (total != 0) {
          entity.setProperty("averageHouseholdIncome", Math.round(sumIncome / total));
          entity.setProperty("crimeRate", Math.round(sumCrimeRate / total));
          entity.setProperty("policeStationRating", Math.round(sumStationRating / total));
        }

        newEntities.add(entity);
      }
    } catch (IOException e) {
      System.out.println("Error parsing the csv");
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          System.out.println("Error closing the reader");
        }
      }
    }
  }
}
