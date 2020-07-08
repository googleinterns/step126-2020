package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.gson.Gson;
import com.google.sps.AssociationResult;
import com.google.sps.data.AssociationData;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to fetch association data with most positive and negative associations */
@WebServlet("/associations")
public class AssociationServlet extends HttpServlet {

  private static final String OUTPUT_TYPE = "applications/json;";
  public static final int LIMIT = 3;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(OUTPUT_TYPE);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(LIMIT);

    Query posQuery =
        new Query(AssociationResult.ENTITY_KIND).addSort("score", SortDirection.DESCENDING);
    ArrayList<String> positive = new ArrayList<String>();
    extractContent(datastore.prepare(posQuery).asQueryResultList(fetchOptions), positive);

    Query negQuery =
        new Query(AssociationResult.ENTITY_KIND).addSort("score", SortDirection.ASCENDING);
    ArrayList<String> negative = new ArrayList<String>();
    extractContent(datastore.prepare(negQuery).asQueryResultList(fetchOptions), negative);

    AssociationData output = new AssociationData(positive, negative);
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(output));
  }

  private void extractContent(QueryResultList<Entity> query, ArrayList<String> output) {
    for (Entity entity : query) {
      output.add((String) entity.getProperty("name"));
    }
  }
}
