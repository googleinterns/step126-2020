package com.google.sps;

import java.util.Comparator;

/** Stores the content of an entity along with it association score */
public class AssociationResult {

  public static final String ENTITY_KIND = "AssociationResult";
  private String content;
  private float score;
  private float weight;

  // epsilon for checking for float equality
  public static final float EPSILON = 0.05f;

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
  }

  public AssociationResult(EntitySentiment entity) {
    content = entity.getContent();
    score = entity.getMagnitude() * entity.getSentiment();
    weight = entity.getMagnitude();
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
