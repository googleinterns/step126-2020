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

/** Extracts sentiments per entity mentions from open text */
public class CloudNLPAssociation implements AutoCloseable {

  private static LanguageServiceClient language;

  /** 
   * Creates a new CloudNLPAssociation object
   * @param language the CloudNLP language server
   */
  public CloudNLPAssociation(LanguageServiceClient language) {
    this.language = language;
  }

  /**
   * Calculates the different entity sentiments in a single entity
   * @param entity the entity to be analyzed
   * @return an arraylist of the different entity sentiments in the entity
   */
  private ArrayList<EntitySentiment> extractEntityMentions(Entity entity) {
    List<EntityMention> mentions = entity.getMentionsList();
    ArrayList<EntitySentiment> res = new ArrayList<EntitySentiment>();
    for (EntityMention mention : mentions) {
      res.add(new EntitySentiment(entity.getName(), mention.getSentiment().getMagnitude(), 
                                  mention.getSentiment().getScore()));
    }
    return res;
  }

  /**
   * Calculates all the entity mentions in a single message
   * @param message the messaged to be analyzed
   * @return an arraylist of all the entity sentiments in the message
   */
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

  /**
   * Analyzes the entity sentiment in all the messages in the input
   * @param messages the open text to be analyzed
   * @return an arraylist of all the entity sentiments in the messages
   */
  public ArrayList<EntitySentiment> analyzeAssociations(ArrayList<String> messages) {
    ArrayList<EntitySentiment> res = new ArrayList<EntitySentiment>();
    for (String message : messages) {
      res.addAll(entitySentimentAnalysis(message));
    }
    return res;
  }

  /**
   * Closes the language server and CloudNLPAssociations object
   */
  @Override
  public void close() {
    language.close();
  }
}