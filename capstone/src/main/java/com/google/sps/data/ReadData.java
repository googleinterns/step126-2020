package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public void readAll() throws IOException {
    File folder = new File("assets/");

    File[] fileNames = folder.listFiles();
    for(File file : fileNames){
       readFile(file);
    }

    sentimentService.close();
  }

  /**
   * Reads the survey csv, splits the lines by a comma delimiter, and adds each element to data
   * store as an entity property
   *
   * @param file This is the file that is being parsed
   * @return {Void}
   */
  public void readFile(File file) {
    String zipCode = file.getName().substring(0, 5);
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(file);
    } catch (FileNotFoundException e) {
      System.out.println("No file found");
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

        if (text.length() > 0) {
          score = sentimentService.getSentiment(text);
        }

        int responseTimeOne = (int)Math.round(Double.parseDouble(values[indexOfInt++]));
        int responseTimeTwo = (int)Math.round(Double.parseDouble(values[indexOfInt++]));
        int responseTimeThree = (int)Math.round(Double.parseDouble(values[indexOfInt]));

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

          if (total != 0) {
            entity.setProperty("averageHouseholdIncome", Math.round(sumIncome / total));
            entity.setProperty("crimeRate", Math.round(sumCrimeRate / total));
            entity.setProperty("policeStationRating", Math.round(sumStationRating / total));
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
