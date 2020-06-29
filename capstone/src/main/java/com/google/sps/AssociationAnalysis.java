package com.google.sps;

import java.util.ArrayList;

public class AssociationAnalysis {

  public ArrayList<AssociationResult> calculateScores(ArrayList<EntitySentiment> sentiments) {
    ArrayList<AssociationResult> res = new ArrayList<AssociationResult>();
    sentiments.forEach(sentiment -> updateScore(res, sentiment));
    return res;
  }

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
