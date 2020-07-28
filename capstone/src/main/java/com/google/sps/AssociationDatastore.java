package com.google.sps;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.sps.data.MapData;
import java.util.ArrayList;
import java.util.Optional;

/** Does all datastore operations related to associations */
public class AssociationDatastore {

  public static final String SURVEY_ENTITY_KIND = "Response";
  public static final String COMMENT_PROPERTY = "text";
  public static final String ZIPCODE = "zipCode";

  private MapData mapData = new MapData();
  private DatastoreService datastore;

  /** @param datastore the datastore client */
  public AssociationDatastore(DatastoreService datastore) {
    this.datastore = datastore;
  }

  /**
   * Get the survey comment text from datastore
   *
   * @return an arraylist of the comment text
   */
  public ArrayList<AssociationInput> getComments() {
    Query query = new Query(SURVEY_ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<AssociationInput> comments = new ArrayList<AssociationInput>();
    for (Entity e : results.asIterable()) {
      if (e.getProperty("association-processed") == null
          || !((boolean) e.getProperty("association-processed"))) {
        String message = (String) e.getProperty(COMMENT_PROPERTY);
        try {
          ArrayList<String> scope = mapData.getScope((String) e.getProperty(ZIPCODE));
          comments.add(new AssociationInput(message, scope));
          e.setProperty("association-processed", true);
          datastore.put(e);
        } catch (NullPointerException exception) {
          System.err.println("Invalid zip code in response: " + (String) e.getProperty(ZIPCODE));
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
  public ArrayList<AssociationResult> loadPreviousResults(String scope) {
    Query query = new Query(AssociationResult.ENTITY_KIND);
    PreparedQuery results = datastore.prepare(query);

    ArrayList<AssociationResult> prev = new ArrayList<AssociationResult>();
    for (Entity entity : results.asIterable()) {
      if (!scope.equals((String) entity.getProperty("scope"))) continue;
      String content = (String) entity.getProperty("name");
      float weight = (float) (double) entity.getProperty("weight");
      float score = (float) (double) entity.getProperty("score");
      boolean strongSentiment = (boolean) entity.getProperty("strong-sentiment");
      Key key = entity.getKey();
      prev.add(new AssociationResult(content, score, weight, strongSentiment, key));
    }

    return prev;
  }

  /**
   * Adds new association results to the datastore service
   *
   * @param res the arraylist of results to be stored
   * @param scope the precinct/scope to store the results under
   */
  public void storeResults(ArrayList<AssociationResult> res, String scope) {
    ArrayList<Entity> entities = new ArrayList<Entity>();
    for (AssociationResult association : res) {
      Optional<Key> key = association.getKey();
      Entity entity;
      if (key.isPresent()) {
        entity = new Entity(key.get());
      } else {
        entity = new Entity(AssociationResult.ENTITY_KIND);
      }
      if (Math.abs(association.getWeight()) < AssociationResult.EPSILON) {
        continue;
      }
      entity.setProperty("name", association.getContent());
      entity.setProperty("score", association.getScore());
      entity.setProperty("weight", association.getWeight());
      entity.setProperty("average-sentiment", association.getAverageSentiment());
      entity.setProperty("strong-sentiment", association.hasStrongSentiment());
      entity.setProperty("scope", scope);
      entities.add(entity);
    }
    datastore.put(entities);
  }
}
