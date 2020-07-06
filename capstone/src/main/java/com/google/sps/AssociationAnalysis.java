package com.google.sps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/** Calculates association scores from a list of sentiments per entity mention */
public class AssociationAnalysis {

  /**
   * Calculates association scores for the input array list of mentions
   *
   * @param sentiments array list of sentiments of the mentions
   * @return the array list of association scores sorted by score
   */
  public ArrayList<AssociationResult> calculateScores(ArrayList<EntitySentiment> sentiments) {
    HashMap<String, AssociationResult> res = new HashMap<String, AssociationResult>();
    sentiments.forEach(sentiment -> updateScore(res, sentiment));
    ArrayList<AssociationResult> list = new ArrayList(res.values());
    Collections.sort(list, AssociationResult.ORDER_BY_SCORE);
    return list;
  }

  /** Updates the result array with the new entity mention given */
  private void updateScore(HashMap<String, AssociationResult> res, EntitySentiment sentiment) {
    float scoreDiff = sentiment.getSentiment() * sentiment.getSignificance();
    AssociationResult association = res.get(sentiment.getContent());
    if (association != null) {
      association.updateScore(association.getScore() + scoreDiff);
    } else {
      res.put(sentiment.getContent(), new AssociationResult(sentiment.getContent(), scoreDiff));
    }
  }
}
