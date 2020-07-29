package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.sps.data.ReadData;
import com.google.sps.data.SentimentData;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    ReadData readData = new ReadData(new SentimentData());
    ArrayList<Entity> allEntities = readData.allEntitiesFromFiles();
    
    for (int i = 0; i < allEntities.size(); i++) {
        Entity currentEntity = allEntities.get(i);
        Entity inStore = null;

        try {
            inStore = datastore.get(currentEntity.getKey());
        } catch (EntityNotFoundException e) {
            inStore = null;
        }

        if (inStore != null) {
            allEntities.remove(i);

            i -= 1;
        }
    }

    datastore.put(allEntities);

    response.sendRedirect("/index.html");
  }
}
