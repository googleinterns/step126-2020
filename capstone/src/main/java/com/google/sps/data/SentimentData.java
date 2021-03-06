package com.google.sps.data;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;

public class SentimentData {
  private LanguageServiceClient languageService;

  public SentimentData() {
    try {
      languageService = LanguageServiceClient.create();
    } catch (IOException e) {
      System.out.println("Client failed to initialize");
    }
  }

  public float getSentiment(String message) throws IOException {
    Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();

    return score;
  }

  public void close() {
    languageService.close();
  }
}
