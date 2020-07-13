package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.sps.data.SentimentData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReadData {
  final String DELIMITER = ",";

  /**
   * Reads the survey csv and adds each element to datastore
   *
   * @return {Void}
   */
  public void readCSV() {
    BufferedReader reader = null;
    FileReader file = null;
    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    SentimentData sentimentService = new SentimentData();

    try {
      file = new FileReader("assets/surveyresponse.csv");
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
        String completion = values[2];
        String gender = values[5];
        String ageRange = values[6];
        String answerOne = values[9];
        String answerTwo = values[10];
        String answerThree = values[12];
        double score = sentimentService.getSentiment(answerThree);
        long responseTimeOne = Long.parseLong(values[13]);
        long responseTimeTwo = Long.parseLong(values[14]);
        long responseTimeThree = Long.parseLong(values[15]);

        Entity entity = new Entity("Response", id);
        Entity inStore; // Entity that may or may not exist in Datastore

        try {
          inStore = datastoreService.get(entity.getKey());
        } catch (EntityNotFoundException e) {
          inStore = null;
        }

        if (inStore == null) {
          entity.setProperty("id", id);
          entity.setProperty("date", date);
          entity.setProperty("completion", completion);
          entity.setProperty("gender", gender);
          entity.setProperty("ageRange", ageRange);
          entity.setProperty("answerOne", answerOne);
          entity.setProperty("answerTwo", answerTwo);
          entity.setProperty("answerThree", answerThree);
          entity.setProperty("score", score);
          entity.setProperty("responseTimeOne", responseTimeOne);
          entity.setProperty("responseTimeTwo", responseTimeTwo);
          entity.setProperty("responseTimeThree", responseTimeThree);

          datastoreService.put(entity);
        }
      }
    } catch (IOException e) {
      System.out.println("IO error");
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          System.out.println("Error closing the reader");
        }
      }
      sentimentService.close();
    }
  }
}
