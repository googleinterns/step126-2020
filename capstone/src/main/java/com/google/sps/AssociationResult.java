package com.google.sps;

import java.lang.Math;

public class AssociationResult {

  private String content;
  private float score;
  public static final float EPSILON = 0.05f;

  public AssociationResult(String content, float score) {
    this.content = content;
    this.score = score;
  }

  public String getContent() {
    return content;
  }

  public float getScore() {
    return score;
  }

  public void updateScore(float newScore) {
    score = newScore;
  }

  @Override
  public String toString() {
    return content + "(" + Float.toString(score) + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    } else if (!AssociationResult.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    AssociationResult x = (AssociationResult) obj;
    return content.equals(x.getContent()) && (Math.abs(x.getScore() - score) < EPSILON);
  }
}
