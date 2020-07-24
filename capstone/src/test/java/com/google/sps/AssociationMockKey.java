package com.google.sps;

public class AssociationMockKey extends AssociationKey {

  private String key;

  @Override
  public String getKey(String content) {
    return content;
  }

  @Override
  public String getKey(AssociationResult result) {
    return result.getContent();
  }

  @Override
  public String getKey(EntitySentiment sentiment) {
    return sentiment.getContent();
  }
}
