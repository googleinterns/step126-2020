package com.google.sps;

import java.util.ArrayList;

/** Calculates association scores from a list of sentiments per entity mention */
public class AssociationAnalysis {

  /** 
   * Calculates association scores for the input array list of mentions
   * @param sentiments array list of sentiments of the mentions
   * @return the array list of association scores 
   * */
  public ArrayList<AssociationResult> calculateScores(ArrayList<EntitySentiment> sentiments) {
    ArrayList<AssociationResult> res = new ArrayList<AssociationResult>();
    sentiments.forEach(sentiment -> updateScore(res, sentiment));
    return res;
  }

   /** Updates the result array with the new entity mention given */
  private void updateScore(ArrayList<AssociationResult> res, EntitySentiment sentiment) {
    float scoreDiff = sentiment.getSentiment() * sentiment.getSignificance();
    for (AssociationResult association : res) {
      if (association.getContent().equals(sentiment.getContent())) {
	association.updateScore(association.getScore() + scoreDiff);
	return;
      }
    }
    res.add(new AssociationResult(sentiment.getContent(), scoreDiff));
  }
}
