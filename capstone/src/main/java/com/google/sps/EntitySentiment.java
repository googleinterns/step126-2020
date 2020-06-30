package com.google.sps;

import java.util.Comparator;

/** Represents data for sentiment around a word/phrase */
public class EntitySentiment {

  private String content;
  private float significance;
  private float sentiment;

  /**
   * Creates a new EntitySentiment object
   * @param content the word/phrase the object refers to
   * @param signficance the importance of the entity in the text
   * @param sentiment the sentiment associated with the entity
   */
  public EntitySentiment(String content, float significance, float sentiment) {
    this.content = content;
    this.significance = significance;
    this.sentiment = sentiment;
  }
  
  /**
   * Returns the content that the entity refers to
   * @return the word/phrase this entity refers to
   **/
  public String getContent() {
    return content;
  }

  /**
   * Returns the importance of this entity in the text
   * @return importance of the entity in the text
   */
  public float getSignificance() {
    return significance;
  }

  /**
   * Returns a sentiment score from -1 (negative) to 1 (positive)
   * @return sentiment score for the entity represented
   */
  public float getSentiment() {
    return sentiment;
  }

  /** Orders alphabetically by content */
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