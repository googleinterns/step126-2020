package com.google.sps;

import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.util.ArrayList;
import java.util.List;

/** Extracts sentiments per entity mentions from open text */
public class CloudNLPAssociation implements AutoCloseable {

  private static LanguageServiceClient language;

  /**
   * Creates a new CloudNLPAssociation object
   *
   * @param language the CloudNLP language server
   */
  public CloudNLPAssociation(LanguageServiceClient language) {
    this.language = language;
  }

  /**
   * Calculates the different entity sentiments in a single entity
   *
   * @param entity the entity to be analyzed
   * @return an arraylist of the different entity sentiments in the entity
   */
  private ArrayList<EntitySentiment> extractEntityMentions(Entity entity, AssociationInput input) {
    List<EntityMention> mentions = entity.getMentionsList();
    ArrayList<EntitySentiment> result = new ArrayList<EntitySentiment>();
    for (EntityMention mention : mentions) {
      result.add(
          new EntitySentiment(
              entity.getName(),
              mention.getSentiment().getMagnitude(),
              mention.getSentiment().getScore(),
              input.getScopes()));
    }
    return result;
  }

  /**
   * Calculates all the entity mentions in a single message
   *
   * @param message the messaged to be analyzed
   * @return an arraylist of all the entity sentiments in the message
   */
  private ArrayList<EntitySentiment> entitySentimentAnalysis(AssociationInput input) {
    Document doc =
        Document.newBuilder().setContent(input.getMessage()).setType(Type.PLAIN_TEXT).build();
    AnalyzeEntitySentimentRequest request =
        AnalyzeEntitySentimentRequest.newBuilder()
            .setDocument(doc)
            .setEncodingType(EncodingType.UTF16)
            .build();
    // detect entity sentiments in the given string
    AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
    List<Entity> entities = response.getEntitiesList();
    ArrayList<EntitySentiment> result = new ArrayList<EntitySentiment>();
    for (Entity entity : entities) {
      result.addAll(extractEntityMentions(entity, input));
    }
    return result;
  }

  /**
   * Analyzes the entity sentiment in all the messages in the input
   *
   * @param messages the open text to be analyzed
   * @return an arraylist of all the entity sentiments in the messages
   */
  public ArrayList<EntitySentiment> analyzeAssociations(ArrayList<AssociationInput> messages) {
    ArrayList<EntitySentiment> result = new ArrayList<EntitySentiment>();
    for (AssociationInput message : messages) {
      result.addAll(entitySentimentAnalysis(message));
    }
    return result;
  }

  /** Closes the language server and CloudNLPAssociations object */
  @Override
  public void close() {
    language.close();
  }
}
