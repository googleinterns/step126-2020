package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReadData {
  final String DELIMITER = ",";
  static final Map<String,ArrayList> zipPrecinctMappint;
  static {
        zipPrecinctMappint = new HashMap<>();
        zipPrecinctMappint.put("94103", new ArrayList<>(
            Arrays.asList("Southern","Mission"));
        zipPrecinctMappint.put("94107", new ArrayList<>(
            Arrays.asList("Bayview","Southern"));
        zipPrecinctMappint.put("94109", new ArrayList<>(
            Arrays.asList("Tenderloin","Central","Northern"));
        zipPrecinctMappint.put("94110", new ArrayList<>(
            Arrays.asList("Ingleside","Mission"));
        zipPrecinctMappint.put("94112", new ArrayList<>(
            Arrays.asList("Ingleside","Taraval"));
        zipPrecinctMappint.put("94114", new ArrayList<>(
            Arrays.asList("Park","Mission"));
        zipPrecinctMappint.put("94116", new ArrayList<>(
            Arrays.asList("Taraval"));
        zipPrecinctMappint.put("94117", new ArrayList<>(
            Arrays.asList("Park","Northern"));
        zipPrecinctMappint.put("94118", new ArrayList<>(
            Arrays.asList("Richmond","Park"));
        zipPrecinctMappint.put("94121", new ArrayList<>(
            Arrays.asList("Richmond"));
        zipPrecinctMappint.put("94122", new ArrayList<>(
            Arrays.asList("Richmond","Taraval","Park"));
        zipPrecinctMappint.put("94123", new ArrayList<>(
            Arrays.asList("Northern"));
        zipPrecinctMappint.put("94124", new ArrayList<>(
            Arrays.asList("Bayview"));
        zipPrecinctMappint.put("94129", new ArrayList<>(
            Arrays.asList("Richmond"));
        zipPrecinctMappint.put("94131", new ArrayList<>(
            Arrays.asList("Ingleside","Park"));
    }


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
