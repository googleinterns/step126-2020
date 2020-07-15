package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.sps.data.MapData;
import com.google.sps.data.SurveyResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that loads data from data store * */
@WebServlet("/load-data")
public class LoadDataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<SurveyResponse> surveyResponses = new ArrayList<SurveyResponse>();
    Query query = new Query("Response");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    String precinct = request.getParameter("precinct");

    ArrayList<String> zipCodes = MapData.getZipCodes(precinct);

    for (Entity e : results.asIterable()) {
      String zipCode = (String) e.getProperty("zipCode");

      if (precinct.length() > 0 && zipCodes.contains(zipCode)) {
        String id = (String) e.getProperty("id");
        Date date = (Date) e.getProperty("date");
        String completion = (String) e.getProperty("completion");
        String gender = (String) e.getProperty("gender");
        String ageRange = (String) e.getProperty("ageRange");
        String answerOne = (String) e.getProperty("answerOne");
        String answerTwo = (String) e.getProperty("answerTwo");
        double score = (double) e.getProperty("score");
        long responseTimeOne = (long) e.getProperty("responseTimeOne");
        long responseTimeTwo = (long) e.getProperty("responseTimeTwo");
        long responseTimeThree = (long) e.getProperty("responseTimeThree");

        SurveyResponse surveyResponse = new SurveyResponse();
        surveyResponse.setZipCode(zipCode);
        surveyResponse.setId(id);
        surveyResponse.setDate(date);
        surveyResponse.setCompletion(completion);
        surveyResponse.setGender(gender);
        surveyResponse.setAgeRange(ageRange);
        surveyResponse.setAnswerOne(answerOne);
        surveyResponse.setAnswerTwo(answerTwo);
        surveyResponse.setScore(score);
        surveyResponse.setResponseTimeOne(responseTimeOne);
        surveyResponse.setResponseTimeTwo(responseTimeTwo);
        surveyResponse.setResponseTimeThree(responseTimeThree);

        surveyResponses.add(surveyResponse);
      }
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(surveyResponses));
  }
}
