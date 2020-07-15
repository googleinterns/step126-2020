package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that resets the association processing status of comments */
@WebServlet("/reset-associations")
public class ResetAssociationServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Response");
    PreparedQuery results = datastore.prepare(query);

    for (Entity e : results.asIterable()) {
      e.setProperty("association-processed", false);
      datastore.put(e);
    }

    response.sendRedirect("/index.html");
  }
}
