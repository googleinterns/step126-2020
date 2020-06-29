package com.google.sps;

import java.util.Comparator;

/** Represents data for sentiment around a word/phrase */
public class EntitySentiment {

  private String content;
  private float significance;
  private float sentiment;

  public EntitySentiment(String content, float significance, float sentiment) {
    this.content = content;
    this.significance = significance;
    this.sentiment = sentiment;
  }

  public String getContent() {
    return content;
  }

  public float getSignificance() {
    return significance;
  }

  public float getSentiment() {
    return sentiment;
  }

  public static final Comparator<EntitySentiment> ORDER_CONTENT = 
    new Comparator<EntitySentiment>() {
      @Override
      public int compare(EntitySentiment a, EntitySentiment b) {
        return a.getContent().compareTo(b.getContent());
      }
    };

  @Override
  public String toString() {
    return content + "(" + Float.toString(significance) + ", " + Float.toString(sentiment) + ")";
  }
}
