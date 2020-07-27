package com.google.sps;

import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.ArrayList;
import java.util.Arrays;
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
}
