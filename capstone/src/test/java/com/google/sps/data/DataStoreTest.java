import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.ReadData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataStoreTest {
  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
   * Creates entities based on real survey file through the ReadData class
   *
   * @param entities Arraylist of entities that will be modified with file data
   * @param file The file that will be parsed to create entities
   * @return void
   */
  public void getEntities(ArrayList<Entity> entities, File file) throws IOException {
    ReadData readData = new ReadData();
    readData.entitiesFromFile(entities, file);
  }

  @Test
  public void testInsertEntity() throws IOException {
    ArrayList<Entity> aEntities = new ArrayList<Entity>();

    File aFile = new File("src/main/webapp/assets/94103.csv");

    getEntities(aEntities, aFile);

    ArrayList<Entity> bEntities = new ArrayList<Entity>();

    File bFile = new File("src/main/webapp/assets/94107.csv");

    getEntities(bEntities, bFile);

    // Entities that will be in datastore
    datastore.put(aEntities);

    // Test storage of old entity A
    Entity a = aEntities.get(0).clone();
    assertTrue(inLocalStore(a));

    // Test insert of new entity B
    Entity b = bEntities.get(0).clone();

    if (!inLocalStore(b)) {
      datastore.put(b);
    }

    assertTrue(inLocalStore(b));
  }
}
