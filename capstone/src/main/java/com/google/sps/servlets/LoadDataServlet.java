package com.google.sps.servlets;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.SurveyResponse;
import java.io.IOException;
import java.lang.ClassCastException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that loads data **/
@WebServlet("/load-data") 
public class LoadDataServlet extends HttpServlet 
{ 
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<SurveyResponse> surveyResponses = new ArrayList<SurveyResponse>();
    Query query = new Query("Response");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
 
    for (Entity e : results.asIterable()) {
        String id = (String) e.getProperty("id");
        double score = (double) e.getProperty("score");
        String gender = (String) e.getProperty("gender");
        String ageRange = (String) e.getProperty("ageRange");
        long responseTime = (long) e.getProperty("responseTime");
        String date = (String) e.getProperty("date");
        long day = (long) e.getProperty("day");
        
        surveyResponses.add(new SurveyResponse(id, score, gender, ageRange, responseTime, date, day));
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(surveyResponses));
  }
}

