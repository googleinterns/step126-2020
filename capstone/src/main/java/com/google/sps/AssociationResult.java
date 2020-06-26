package com.google.sps;

class AssociationResult {

  private String content;
  private float score;

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
}
