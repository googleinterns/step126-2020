package com.google.sps;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.sps.data.MapData;
import java.io.IOException;
import java.util.ArrayList;
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

  public UpdateAssociationServlet() {
    keyGeneration = new AssociationKey();
  }

  public void init() throws ServletException {
    try {
      nlpClient = LanguageServiceClient.create();
      datastore = new AssociationDatastore(DatastoreServiceFactory.getDatastoreService());
    } catch (IOException exception) {
      System.err.println(exception);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    CloudNLPAssociation nlp = new CloudNLPAssociation(nlpClient);

    ArrayList<EntitySentiment> sentiments = nlp.analyzeAssociations(datastore.getComments());

    for (String scope : mapData.getAllScopes()) {
      AssociationAnalysis analysis =
          new AssociationAnalysis(keyGeneration, datastore.loadPreviousResults(scope));
      ArrayList<EntitySentiment> filteredSentiment = new ArrayList<EntitySentiment>();
      for (EntitySentiment sentiment : sentiments) {
        if (sentiment.getScopes().contains(scope)) {
          filteredSentiment.add(sentiment);
        }
      }
      ArrayList<AssociationResult> res = analysis.calculateScores(filteredSentiment);
      datastore.storeResults(res, scope);
    }
  }

  public void destroy() {
    nlpClient.close();
  }
}
