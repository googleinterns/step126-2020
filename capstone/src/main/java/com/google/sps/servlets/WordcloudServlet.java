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
import com.google.sps.data.WordcloudData;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to fetch association data with most positive and negative associations */
@WebServlet("/wordcloud")
public class WordcloudServlet extends HttpServlet {

  private static final String OUTPUT_TYPE = "applications/json;";

  private static final float MAX_SIZE = 200f;

  private DatastoreService datastore;

  public void init() throws ServletException {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(OUTPUT_TYPE);

    FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();

    String scope = request.getParameter("scope");
    if (scope == null) {
      scope = "SF";
    }

    Query query =
        new Query(AssociationResult.ENTITY_KIND).addSort("score", SortDirection.ASCENDING);
    ArrayList<WordcloudData> result =
        extractContent(datastore.prepare(query).asQueryResultList(fetchOptions), scope);

    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(result));
  }

  /**
   * Returns the word cloud values pairs
   *
   * @param query the entities to get the names from
   * @return the arraylist of word cloud data
   */
  private ArrayList<WordcloudData> extractContent(QueryResultList<Entity> query, String scope) {
    if (query.size() == 0) return new ArrayList<WordcloudData>();
    ArrayList<WordcloudData> output = new ArrayList<WordcloudData>();
    for (Entity entity : query) {
      if (!scope.equals((String) entity.getProperty("scope"))) continue;
      float weight = Math.abs((float) (double) entity.getProperty("weight"));
      float gradient = (float) (double) entity.getProperty("average-sentiment");
      String content = (String) entity.getProperty("name");
      output.add(new WordcloudData(content, weight, gradient));
    }
    return output;
  }
}
