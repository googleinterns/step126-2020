package com.google.sps;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.sps.data.MapData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update-associations")
public class UpdateAssociationServlet extends HttpServlet {

  public static final String SURVEY_ENTITY_KIND = "Response";
  public static final String COMMENT_PROPERTY = "answerThree";
  public static final String ZIPCODE = "zipCode";

  private LanguageServiceClient nlpClient;
  private DatastoreService datastore;
  private static final List<String> SCOPES =
      Arrays.asList(
          "SF",
          "Southern",
          "Mission",
          "Bayview",
          "Tenderloin",
          "Central",
          "Northern",
          "Ingleside",
          "Taraval",
          "Park",
          "Richmond");

  public void init() throws ServletException {
    try {
      nlpClient = LanguageServiceClient.create();
      datastore = DatastoreServiceFactory.getDatastoreService();
    } catch (IOException exception) {
      System.err.println(exception);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    CloudNLPAssociation nlp = new CloudNLPAssociation(nlpClient);

    ArrayList<EntitySentiment> sentiments = nlp.analyzeAssociations(getComments());

    for (String scope : SCOPES) {
      AssociationAnalysis analysis = new AssociationAnalysis(loadPreviousResults(scope));
      ArrayList<EntitySentiment> filteredSentiment = new ArrayList<EntitySentiment>();
      for (EntitySentiment sentiment : sentiments) {
        if (sentiment.getScopes().contains(scope)) {
          filteredSentiment.add(sentiment);
        }
      }
      ArrayList<AssociationResult> res = analysis.calculateScores(filteredSentiment);
      storeResults(res, scope);
    }
  }

  public void destroy() {
    nlpClient.close();
  }

  /**
   * Get the survey comment text from datastore
   *
   * @return an arraylist of the comment text
   */
  private ArrayList<AssociationInput> getComments() {
    Query query = new Query(SURVEY_ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<AssociationInput> comments = new ArrayList<AssociationInput>();
    for (Entity e : results.asIterable()) {
      if (e.getProperty("association-processed") == null
          || !((boolean) e.getProperty("association-processed"))) {
        String message = (String) e.getProperty(COMMENT_PROPERTY);
        try {
          ArrayList<String> scope =
              (ArrayList<String>)
                  (new MapData()).getPrecincts((String) e.getProperty(ZIPCODE)).clone();
          scope.add("SF");
          comments.add(new AssociationInput(message, scope));
          e.setProperty("association-processed", true);
          datastore.put(e);
        } catch (NullPointerException exception) {
          System.err.println("Invalid zip code in response");
        }
      }
    }
    return comments;
  }

  /**
   * Loads all previous association results from datastore
   *
   * @param scope the scope to load results from
   * @return an arraylist of previous responses
   */
  private ArrayList<AssociationResult> loadPreviousResults(String scope) {
    Query query = new Query(AssociationResult.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<AssociationResult> prev = new ArrayList<AssociationResult>();
    for (Entity entity : results.asIterable()) {
      if (!scope.equals((String) entity.getProperty("scope"))) continue;
      String content = (String) entity.getProperty("name");
      float weight = (float) (double) entity.getProperty("weight");
      float score = (float) (double) entity.getProperty("score");
      Key key = entity.getKey();
      prev.add(new AssociationResult(content, score, weight, key));
    }

    return prev;
  }

  /**
   * Adds new association results to the datastore service
   *
   * @param res the arraylist of results to be stored
   * @param scope the precinct/scope to store the results under
   */
  private void storeResults(ArrayList<AssociationResult> res, String scope) {
    ArrayList<Entity> entities = new ArrayList<Entity>();
    for (AssociationResult association : res) {
      Optional<Key> key = association.getKey();
      Entity entity;
      if (key.isPresent()) {
        entity = new Entity(key.get());
      } else {
        entity = new Entity(AssociationResult.ENTITY_KIND);
      }
      entity.setProperty("name", association.getContent());
      entity.setProperty("score", association.getScore());
      entity.setProperty("weight", association.getWeight());
      entity.setProperty("average-sentiment", association.getAverageSentiment());
      entity.setProperty("scope", scope);
      entities.add(entity);
    }
    datastore.put(entities);
  }
}
