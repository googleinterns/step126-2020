package com.google.sps;

import java.util.ArrayList;
import java.util.Comparator;

/** Represents data for sentiment around a word/phrase */
public class EntitySentiment {

  private String content;
  private float magnitude;
  private float sentiment;
  private ArrayList<String> scopes;

  /**
   * Creates a new EntitySentiment object
   *
   * @param content the word/phrase the object refers to
   * @param magnitude the importance of the entity in the text
   * @param sentiment the sentiment associated with the entity
   */
  public EntitySentiment(String content, float magnitude, float sentiment) {
    this.content = content;
    this.magnitude = magnitude;
    this.sentiment = sentiment;
    this.scopes = new ArrayList<String>();
  }

  /**
   * Creates a new EntitySentiment object
   *
   * @param content the word/phrase the object refers to
   * @param magnitude the importance of th entity in the text
   * @param sentiment the sentiment associated with the entity
   * @param scopes the scopes within this entity is valid
   */
  public EntitySentiment(
      String content, float magnitude, float sentiment, ArrayList<String> scopes) {
    this.content = content;
    this.magnitude = magnitude;
    this.sentiment = sentiment;
    this.scopes = scopes;
  }

  /**
   * Returns the content that the entity refers to
   *
   * @return the word/phrase this entity refers to
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the importance of this entity in the text
   *
   * @return importance of the entity in the text
   */
  public float getMagnitude() {
    return magnitude;
  }

  /**
   * Returns a sentiment score from -1 (negative) to 1 (positive)
   *
   * @return sentiment score for the entity represented
   */
  public float getSentiment() {
    return sentiment;
  }

  public String getKey () {
    return content.toLowerCase().trim(); 
  }

  /**
   * Returns the scopes within this entity is valid
   *
   * @return scopes within which this entity is valid
   */
  public ArrayList<String> getScopes() {
    return scopes;
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
    return content + "(" + Float.toString(magnitude) + ", " + Float.toString(sentiment) + ")";
  }
}
