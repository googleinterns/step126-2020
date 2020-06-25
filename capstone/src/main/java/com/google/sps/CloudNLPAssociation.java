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
import java.util.ArrayList;
import java.util.stream.Stream;

class CloudNLPAssociation {

  private static LanguageServiceClient language;

  private static Stream<EntitySentiment> extractSingleEntityMentions(Entity entity) {
    Stream<EntityMention> mentions = entity.getMentionsList().stream();
    return mentions.map(mention -> {
        return new EntitySentiment(entity.getName(), mention.getSentiment().getMagnitude(), mention.getSentiment().getScore());
      });
  }

  private static Stream<EntitySentiment> extractEntityMentions(Stream<Entity> entities) {
    return entities.flatMap(entity -> extractSingleEntityMentions(entity)); 
  }

  private static Stream<EntitySentiment> entitySentimentAnalysis(String message) {
    Document doc = Document.newBuilder().setContent(message).setType(Type.PLAIN_TEXT).build();
    AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();
    // detect entity sentiments in the given string
    AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
    Stream<Entity> entities = response.getEntitiesList().stream();
    return entities.flatMap(entity -> extractEntityMentions(entities));
  }

  public static Stream<EntitySentiment> analyzeAssociations(ArrayList<String> messages) throws IOException {
    Stream<String> messageStream = messages.stream();
    language = LanguageServiceClient.create();
    Stream<EntitySentiment> result = messageStream.flatMap(message -> entitySentimentAnalysis(message));
    language.close();
    return result;
  }
}
