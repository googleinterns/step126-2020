import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.ReadData;
import com.google.sps.data.SentimentData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataStoreTest {
  private final DatastoreService datastore =
      DatastoreServiceFactory.getDatastoreService();
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

  // Run this test twice to prove we're not leaking any state across tests.
  private void doTest() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    assertEquals(0, ds.prepare(new Query("test")).countEntities(withLimit(10)));
    ds.put(new Entity("test"));
    ds.put(new Entity("test"));
    assertEquals(2, ds.prepare(new Query("test")).countEntities(withLimit(10)));
  }

  @Test
  public void testStateleakInsert1() {
    doTest();
  }

  @Test
  public void testStateleakInsert2() {
    doTest();
  }

  public boolean inLocalStore(Entity entity) {
    try {
      datastore.get(entity.getKey());
    } catch (EntityNotFoundException e) {
      return false;
    }
    
    return true;
  }

   /**
   * Creates an entity based on given survey response values
   *
   * @param kind Namespace in datastore
   * @param zipCode Location of where survey was taken
   * @param id Unique id given to each survey
   * @param score Sentiment score for the survey response
   * @return Entity data store object representing a survey response
   */
  public Entity getMockEntity(String kind, String zipCode, int id, float score) {
   Entity entity = new Entity(kind, id);
   entity.setProperty("zipCode", zipCode);
   entity.setProperty("score", score);  

   return entity;
  }

  @Test
  public void testMockEntityStorage() throws IOException {
    ArrayList<Entity> testEntities = new ArrayList<Entity>();
    Entity a = getMockEntity("ResponseTest", "94101", 1, 0.5f);
    Entity b = getMockEntity("ResponseTest", "94151", 2, 0.83f);
    
    testEntities.add(a);
    testEntities.add(b);

    datastore.put(testEntities);
    
    Entity entity = new Entity("ResponseTest", 2);
    

    assertTrue(inLocalStore(entity));
  }

   /**
   * Creates entities based on real survey file through
   * the ReadData class
   *
   * @return ArrayList<Entity> data store objects initailized with
   * the data from all files
   */
  public ArrayList<Entity> getEntities() throws IOException {
   ReadData readData = new ReadData(new SentimentData());
   
   return readData.allEntitiesFromFiles();
  }

  @Test
  public void testInsertNewEntity() throws IOException { 
    // All entities to consider
    ArrayList<Entity> allEntities = getEntities();
    
    //Test insert entity
    Entity outStoreEntity = allEntities.remove(0);
    
    //Entities that will be in datastore
    ArrayList<Entity> inStoreEntities = allEntities;

    datastore.put(inStoreEntities);
    
    if (!inLocalStore(outStoreEntity)) {
        datastore.put(outStoreEntity);
    }

    assertTrue(inLocalStore(outStoreEntity));
  }
}
