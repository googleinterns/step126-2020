package com.google.sps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AssociationDatastoreTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  private void addComment(
      DatastoreService datastore, String text, String zipCode, boolean associationProcessed) {
    Entity entity = new Entity(AssociationDatastore.SURVEY_ENTITY_KIND);
    entity.setProperty(AssociationDatastore.COMMENT_PROPERTY, text);
    entity.setProperty(AssociationDatastore.ZIPCODE, zipCode);
    entity.setProperty("association-processed", associationProcessed);
    datastore.put(entity);
  }

  @Test
  public void getCommentsEmptyTest() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    AssociationDatastore association = new AssociationDatastore(datastore);
    ArrayList<AssociationInput> input = association.getComments();
    assertEquals(input, new ArrayList<AssociationInput>());
  }

  @Test
  public void skipsProcessedEntriesTest() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    addComment(datastore, "hi", "94123", true);
    addComment(datastore, "test", "94124", true);
    addComment(datastore, "testing", "94122", true);

    AssociationDatastore association = new AssociationDatastore(datastore);
    ArrayList<AssociationInput> input = association.getComments();
    assertEquals(input, new ArrayList<AssociationInput>());
  }

  @Test
  public void skipsProcessedReturnsNewTest() {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    addComment(datastore, "hi", "94123", true);
    addComment(datastore, "test", "94124", false);
    addComment(datastore, "testing", "94122", true);

    AssociationDatastore association = new AssociationDatastore(datastore);
    ArrayList<AssociationInput> input = association.getComments();
    assertEquals(
        input,
        new ArrayList<AssociationInput>(
            Arrays.asList(
                new AssociationInput(
                    "test", new ArrayList<String>(Arrays.asList("Bayview", "SF"))))));
  }

  /**
   * Calculates whether the list of results is a valid list of results (has at most one response per
   * word
   *
   * @param results the list of results to be checked for validity
   * @return whether the list is a valid list of results
   */
  public boolean isValidAssociationDatastore(ArrayList<AssociationResult> results) {
    HashSet<String> seen = new HashSet<String>();
    for (AssociationResult result : results) {
      if (seen.contains(result.getContent())) {
        return false;
      }
      seen.add(result.getContent());
    }
    return true;
  }

  @Test
  public void addThenRetrieveTest() {
    ArrayList<AssociationResult> data =
        new ArrayList<AssociationResult>(
            Arrays.asList(
                new AssociationResult("hi", 12.4f, 118.0f, false),
                new AssociationResult("test1", 1.3f, 123.4f, true)));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    AssociationDatastore association = new AssociationDatastore(datastore);
    association.storeResults(data, "SF");
    ArrayList<AssociationResult> retrieved = association.loadPreviousResults("SF");
    assertTrue("Has multiple results for the same word", isValidAssociationDatastore(retrieved));
    assertEquals(data, retrieved);
  }

  @Test
  public void addThenRetrieveDifferentScopeTest() {
    ArrayList<AssociationResult> data =
        new ArrayList<AssociationResult>(
            Arrays.asList(
                new AssociationResult("hi", 12.4f, 118.0f, false),
                new AssociationResult("test1", 1.3f, 123.4f, true)));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    AssociationDatastore association = new AssociationDatastore(datastore);
    association.storeResults(data, "Bayview");
    ArrayList<AssociationResult> retrieved = association.loadPreviousResults("SF");
    assertTrue("Has multiple results for the same word", isValidAssociationDatastore(retrieved));
    assertEquals(new ArrayList<AssociationResult>(), retrieved);
  }

  @Test
  public void dataPersistenceTest() {
    ArrayList<AssociationResult> data =
        new ArrayList<AssociationResult>(
            Arrays.asList(
                new AssociationResult("hi", 12.4f, 118.0f, false),
                new AssociationResult("test1", 1.3f, 123.4f, true)));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    AssociationDatastore association = new AssociationDatastore(datastore);
    association.storeResults(data, "SF");
    AssociationDatastore newAssociation = new AssociationDatastore(datastore);
    ArrayList<AssociationResult> retrieved = newAssociation.loadPreviousResults("SF");
    assertTrue("Has multiple results for the same word", isValidAssociationDatastore(retrieved));
    assertEquals(data, retrieved);
  }

  @Test
  public void doubleAddTest() {
    ArrayList<AssociationResult> data =
        new ArrayList<AssociationResult>(
            Arrays.asList(
                new AssociationResult("hi", 12.4f, 118.0f, false),
                new AssociationResult("test1", 1.3f, 123.4f, true)));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    AssociationDatastore association = new AssociationDatastore(datastore);
    association.storeResults(data, "SF");
    ArrayList<AssociationResult> retrieved = association.loadPreviousResults("SF");
    assertTrue("Has multiple results for the same word", isValidAssociationDatastore(retrieved));
    assertEquals(data, retrieved);
    association.storeResults(retrieved, "SF");
    retrieved = association.loadPreviousResults("SF");
    assertTrue("Has multiple results for the same word", isValidAssociationDatastore(retrieved));
    assertEquals(data, retrieved);
  }
}
