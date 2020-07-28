package com.google.sps.servlet;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.sps.AssociationDatastore;
import com.google.sps.AssociationKey;
import com.google.sps.UpdateAssociation;
import com.google.sps.data.MapData;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update-associations")
public class UpdateAssociationServlet extends HttpServlet {

  public static final String SURVEY_ENTITY_KIND = "Response";
  public static final String COMMENT_PROPERTY = "text";
  public static final String ZIPCODE = "zipCode";

  private LanguageServiceClient nlpClient;
  private AssociationDatastore datastore;
  private MapData mapData = new MapData();
  private AssociationKey keyGeneration;
  private UpdateAssociation association;

  public UpdateAssociationServlet() {}

  public void init() throws ServletException {
    try {
      nlpClient = LanguageServiceClient.create();
      datastore = new AssociationDatastore(DatastoreServiceFactory.getDatastoreService());
      keyGeneration = new AssociationKey();
      association = new UpdateAssociation(datastore, nlpClient, keyGeneration);
    } catch (IOException exception) {
      System.err.println(exception);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    association.update();
  }

  public void destroy() {
    nlpClient.close();
  }
}
