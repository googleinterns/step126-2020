package com.google.sps;

import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.sps.data.MapData;
import java.util.ArrayList;

public class UpdateAssociation {

  public static final String SURVEY_ENTITY_KIND = "Response";
  public static final String COMMENT_PROPERTY = "text";
  public static final String ZIPCODE = "zipCode";

  private LanguageServiceClient nlpClient;
  private AssociationDatastore datastore;
  private MapData mapData = new MapData();
  private AssociationKey keyGeneration;

  public UpdateAssociation(
      AssociationDatastore datastore,
      LanguageServiceClient nlpClient,
      AssociationKey keyGeneration) {
    this.datastore = datastore;
    this.nlpClient = nlpClient;
    this.keyGeneration = keyGeneration;
  }

  public void update() {
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
}
