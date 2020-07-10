package com.google.sps;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update-associations")
public class UpdateAssociationServlet extends HttpServlet {

  public static final String SURVEY_ENTITY_KIND = "Actual";
  public static final String COMMENT_PROPERTY = "text";

  private LanguageServiceClient nlpClient;
  private DatastoreService datastore;

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
    AssociationAnalysis association = new AssociationAnalysis(loadPreviousResults());
    CloudNLPAssociation nlp = new CloudNLPAssociation(nlpClient);
    ArrayList<AssociationResult> res =
        association.calculateScores(nlp.analyzeAssociations(getComments()));
    response.getWriter().println(res);

    clearPreviousResults();
    storeResults(res);
  }

  public void destroy() {
    nlpClient.close();
  }

  /**
   * Get the survey comment text from datastore
   *
   * @return an arraylist of the comment text
   */
  private ArrayList<String> getComments() {
    Query query = new Query(SURVEY_ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> comments = new ArrayList<String>();
    for (Entity e : results.asIterable()) {
      if (e.getProperty("association-processed") == null
          || !((boolean) e.getProperty("association-processed"))) {
        comments.add((String) e.getProperty(COMMENT_PROPERTY));
        e.setProperty("association-processed", true);
	datastore.put(e);
      }
    }
    return comments;
  }

  /** Removes all previous association results from datastore */
  private void clearPreviousResults() {
    Query query = new Query(AssociationResult.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Key> toDelete = new ArrayList<Key>();
    for (Entity entity : results.asIterable()) {
      toDelete.add(entity.getKey());
    }

    datastore.delete(toDelete);
  }

  /**
   * Loads all previous association results from datastore
   *
   * @return an arraylist of previous responses
   */
  private ArrayList<AssociationResult> loadPreviousResults() {
    Query query = new Query(AssociationResult.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<AssociationResult> prev = new ArrayList<AssociationResult>();
    for (Entity entity : results.asIterable()) {
      String content = (String) entity.getProperty("name");
      float weight = (float) (double) entity.getProperty("weight");
      float score = (float) (double) entity.getProperty("score");
      prev.add(new AssociationResult(content, score, weight));
    }

    return prev;
  }

  /**
   * Adds new association results to the datastore service
   *
   * @param res the arraylist of results to be stored
   */
  private void storeResults(ArrayList<AssociationResult> res) {
    ArrayList<Entity> entities = new ArrayList<Entity>();
    for (AssociationResult association : res) {
      Entity entity = new Entity(AssociationResult.ENTITY_KIND);
      entity.setProperty("name", association.getContent());
      entity.setProperty("score", association.getScore());
      entity.setProperty("weight", association.getWeight());
      entity.setProperty("average-sentiment", association.getAverageSentiment());
      entities.add(entity);
    }
    datastore.put(entities);
  }
}
