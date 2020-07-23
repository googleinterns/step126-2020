package com.google.sps;

import com.google.sps.AssociationKey;
import com.google.sps.AssociationResult;
import com.google.sps.EntitySentiment;

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
