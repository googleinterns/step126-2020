package com.google.sps;

import java.util.Comparator;

/** Stores the content of an entity along with it association score */
public class AssociationResult {

  private String content;
  private float score;

  // epsilon for checking for float equality
  public static final float EPSILON = 0.05f;

  /**
   * Creates an AssociationResult object
   *
   * @param content the word/phrase for which the result is for
   * @param score an intial score for the word/phrase from -1 (neg) to 1 (pos)
   */
  public AssociationResult(String content, float score) {
    this.content = content;
    this.score = score;
  }

  /** @return the word/phrase that the score is for */
  public String getContent() {
    return content;
  }

  /** @return the current score of the word/phrase from -1 (neg) to 1 (pos) */
  public float getScore() {
    return score;
  }

  /**
   * Updates the score to a new score
   *
   * @param newScore the new score to be set to
   */
  public void updateScore(float newScore) {
    score = newScore;
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
    return content.equals(x.getContent()) && (Math.abs(x.getScore() - score) < EPSILON);
  }
}
