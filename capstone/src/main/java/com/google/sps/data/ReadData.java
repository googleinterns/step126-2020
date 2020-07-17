package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class ReadData {
  private DatastoreService datastoreService;
  private SentimentData sentimentService;
  private ArrayList<Entity> newEntities;
  private final String DELIMITER = ",";

  public ReadData() throws IOException {
    datastoreService = DatastoreServiceFactory.getDatastoreService();
    sentimentService = new SentimentData();
    newEntities = new ArrayList<Entity>();
  }

  /**
   * Calls read file while iterating through all the zip codes
   *
   * @return {Void}
   */
  public void readAll() {
    Set<String> zipCodes = MapData.zipPrecinctMap.keySet();

    for (String zip : zipCodes) {
      String filePath = "assets/" + zip + ".csv";

      try {
        readFile(new FileReader(filePath), zip);
      } catch (FileNotFoundException e) {
        System.out.println(filePath + " is not in the assets folder");
      }
    }

    sentimentService.close();
  }

  /**
   * Reads the survey csv, splits the lines by a comma delimiter, and adds each element to data
   * store as an entity property
   *
   * @param file This is the file that is being parsed
   * @param zip This is the zip code the file is associated with
   * @return {Void}
   */
  public void readFile(FileReader file, String zipCode) {
    final int EXPECTED_LENGTH = 15;
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(file);
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

        int indexOfLong = (values.length - EXPECTED_LENGTH) + START_INDEX + 1;

        for (int i = START_INDEX; i < indexOfLong; i++) {
          text += values[i];
        }

        float score = 0;

        if (text.length() > 0) {
          score = sentimentService.getSentiment(text);
        }

        long responseTimeOne = Long.parseLong(values[indexOfLong++]);
        long responseTimeTwo = Long.parseLong(values[indexOfLong++]);
        long responseTimeThree = Long.parseLong(values[indexOfLong]);

        Entity entity = new Entity("Response", id);
        Entity inStore; // Entity that may or may not exist in Datastore

        try {
          inStore = datastoreService.get(entity.getKey());
        } catch (EntityNotFoundException e) {
          inStore = null;
        }

        if (inStore == null) {
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

          // Set properties pertaining to the precinct the survey maps to
          String precinctName = MapData.getPopulationPrecinct(zipCode);
          Precinct precinctData = FeatureData.getPrecinct(precinctName);

          if (precinctData != null) {
            entity.setProperty("averageHouseholdIncome", precinctData.getAverageHouseholdIncome());
            entity.setProperty("crimeRate", precinctData.getCrimeRate());
            entity.setProperty("policeStationRating", precinctData.getPoliceStationRating());
          }

          newEntities.add(entity);
        }
      }

      datastoreService.put(newEntities);

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
