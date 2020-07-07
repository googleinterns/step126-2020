package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadData {
  final String DELIMITER = ",";

  public void readCSV() {
    BufferedReader reader = null;
    FileReader file = null;
    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

    try {
      file = new FileReader("assets/surveyresponse.csv");
      reader = new BufferedReader(file);
      String header = reader.readLine();
      String line = "";

      while ((line = reader.readLine()) != null) {
        String[] values = line.split(DELIMITER);
        String id = values[0];
        double score = Double.parseDouble(values[2]);
        String gender = values[3];
        String ageRange = values[4];
        long responseTime = Long.parseLong(values[5]);
        String date = values[6];
        long day = Long.parseLong(values[7]);

        Entity entity = new Entity("Response", id);
        Entity inStore; // Entity that may or may not exist in Datastore

        try {
          inStore = datastoreService.get(entity.getKey());
        } catch (EntityNotFoundException e) {
          inStore = null;
        }

        if (inStore == null) {
          entity.setProperty("id", id);
          entity.setProperty("score", score);
          entity.setProperty("gender", gender);
          entity.setProperty("ageRange", ageRange);
          entity.setProperty("responseTime", responseTime);
          entity.setProperty("date", date);
          entity.setProperty("day", day);

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
    }
  }
}
