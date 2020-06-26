package com.google.sps;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.IOException;
import java.lang.AutoCloseable;
import java.util.ArrayList;
import java.util.List;

class CloudNLPAssociation implements AutoCloseable {

  private static LanguageServiceClient language;

  public CloudNLPAssociation(LanguageServiceClient language) throws IOException {
    this.language = language;
  }

  private ArrayList<EntitySentiment> extractEntityMentions(Entity entity) {
    List<EntityMention> mentions = entity.getMentionsList();
    ArrayList<EntitySentiment> res = new ArrayList<EntitySentiment>();
    for (EntityMention mention : mentions) {
      res.add(new EntitySentiment(entity.getName(), mention.getSentiment().getMagnitude(), 
                                  mention.getSentiment().getScore()));
    }
    return res;
  }

  private ArrayList<EntitySentiment> entitySentimentAnalysis(String message) {
    Document doc = Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
    AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();
    // detect entity sentiments in the given string
    AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
    List<Entity> entities = response.getEntitiesList();
    ArrayList<EntitySentiment> res = new ArrayList<EntitySentiment>();
    for (Entity entity : entities) {
      res.addAll(extractEntityMentions(entity));
    }
    return res;
  }

  public ArrayList<EntitySentiment> analyzeAssociations(ArrayList<String> messages) {
    ArrayList<EntitySentiment> res = new ArrayList<EntitySentiment>();
    for (String message : messages) {
      res.addAll(entitySentimentAnalysis(message));
    }
    return res;
  }

  @Override
  public void close() {
    if (language != null) {
      language.close();
    }
  }
}
