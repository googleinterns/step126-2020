package com.google.sps;

import com.google.appengine.api.datastore.Key;
import java.util.Comparator;
import java.util.Optional;

/** Stores the content of an entity along with it association score */
public class AssociationResult {

  public static final String ENTITY_KIND = "AssociationResult";
  private String content;
  private float score;
  private float weight;
  private Optional<Key> key;

  // epsilon for checking for float equality
  public static final float EPSILON = 0.05f;

  /**
   * Creates an AssociationResult object
   *
   * @param content the word/phrase for which the result is for
   * @param score an initial score for the word/phrase from -1 (neg) to 1 (pos)
   * @param weight the weight of the sentiment
   * @param key the key of the current result in datastore
   */
  public AssociationResult(String content, float score, float weight, Key key) {
    this.content = content;
    this.score = score;
    this.weight = weight;
    this.key = Optional.of(key);
  }

  /**
   * Creates an AssociationResult object
   *
   * @param content the word/phrase for which the result is for
   * @param score an intial score for the word/phrase from -1 (neg) to 1 (pos)
   * @param weight the weight of the sentiment
   */
  public AssociationResult(String content, float score, float weight) {
    this.content = content;
    this.score = score;
    this.weight = weight;
    key = Optional.empty();
  }

  /**
   * Crates an AssociationResult object
   *
   * @param entity the intial entity that is part of the result
   */
  public AssociationResult(EntitySentiment entity) {
    content = entity.getContent();
    score = entity.getMagnitude() * entity.getSentiment();
    weight = entity.getMagnitude();
    key = Optional.empty();
  }

  /** @return the word/phrase that the score is for */
  public String getContent() {
    return content;
  }

  /** @return the current score of the word/phrase from -1 (neg) to 1 (pos) */
  public float getScore() {
    return score;
  }

  /** @return the average sentiment weighted by magnitude * */
  public float getAverageSentiment() {
    return weight == 0 ? 0 : (score / weight);
  }

  /** @return the weight of the overal sentiment */
  public float getWeight() {
    return weight;
  }

  /** @return the location of the result in datastore (empty optional if none exists */
  public Optional<Key> getKey() {
    return key;
  }

  public String getNormalizedString() {
    return content.toLowerCase().trim();
  }

  /**
   * Updates the score to a new score
   *
   * @param entity the new entity to be added to the pool creating the result
   */
  public void updateResult(EntitySentiment entity) {
    score += entity.getMagnitude() * entity.getSentiment();
    weight += entity.getMagnitude();
  }

  public static final Comparator<AssociationResult> ORDER_BY_SCORE =
      new Comparator<AssociationResult>() {
        @Override
        public int compare(AssociationResult a, AssociationResult b) {
          return Float.compare(a.getScore(), b.getScore());
        }
      };

  @Override
  public String toString() {
    return content + "(" + Float.toString(score) + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !AssociationResult.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    AssociationResult x = (AssociationResult) obj;
    return content.equals(x.getContent())
        && (Math.abs(x.getScore() - score) < EPSILON)
        && (Math.abs(x.getWeight() - weight) < EPSILON);
  }
}
