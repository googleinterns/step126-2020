package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
/** Servlet that adds comments to datastore */
@WebServlet("/delete-data") 
public class DeleteDataServlet extends HttpServlet 
{ 
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Response");
    PreparedQuery results = datastore.prepare(query);
    List<Key> removeEntires = new ArrayList<Key>();

    for (Entity e: results.asIterable()) {
       removeEntires.add(e.getKey());
    }
    datastore.delete(removeEntires);

    response.sendRedirect("/index.html");
  }
}
