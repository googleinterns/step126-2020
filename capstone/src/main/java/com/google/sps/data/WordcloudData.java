package com.google.sps.data;


/** Data for associations wordcloud */
public final class WordcloudData {

  private String content;
  private float weight;
  private float gradient;

  public WordcloudData(String content, float weight, float gradient) {
    this.content = content;
    this.weight = weight;
    this.gradient = gradient;
  }

  public String getContent() {
    return content;
  }

  public float getWeight() {
    return weight;
  }

  public float getGradient() {
    return gradient;
  }
}
