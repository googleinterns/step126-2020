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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update-associations")
public class UpdateAssociationServlet extends HttpServlet {

  public static final String SURVEY_ENTITY_KIND = "Actual";
  public static final String COMMENT_PROPERTY = "text";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    response.setContentType("text/html;");
    AssociationAnalysis association = new AssociationAnalysis();
    CloudNLPAssociation nlp = new CloudNLPAssociation(LanguageServiceClient.create());
    ArrayList<AssociationResult> res =
        association.calculateScores(nlp.analyzeAssociations(getComments(datastore)));
    response.getWriter().println(res);

    clearPreviousResults(datastore);
    storeResults(datastore, res);

    nlp.close();
  }

  private ArrayList<String> getComments(DatastoreService datastore) {
    Query query = new Query(SURVEY_ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<String> comments = new ArrayList<String>();
    for (Entity e : results.asIterable()) {
      comments.add((String) e.getProperty(COMMENT_PROPERTY));
    }
    return comments;
  }

  private void clearPreviousResults(DatastoreService datastore) {
    Query query = new Query(AssociationResult.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Key> toDelete = new ArrayList<Key>();
    for (Entity entity : results.asIterable()) {
      toDelete.add(entity.getKey());
    }

    datastore.delete(toDelete);
  }

  private void storeResults(DatastoreService datastore, ArrayList<AssociationResult> res) {
    for (AssociationResult association : res) {
      Entity entity = new Entity(AssociationResult.ENTITY_KIND);
      entity.setProperty("name", association.getContent());
      entity.setProperty("score", association.getScore());
      datastore.put(entity);
    }
  }
}
