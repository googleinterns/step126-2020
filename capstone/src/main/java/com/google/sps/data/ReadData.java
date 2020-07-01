package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.sps.data.SurveyResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.NullPointerException;

public class ReadData {
    public void readCSV() {
        SurveyResponse response;
        BufferedReader reader;
        FileReader file;
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        final String DELIMITER = ",";

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

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}