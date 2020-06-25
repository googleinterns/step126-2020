package com.google.sps;

import java.util.ArrayList;
import java.util.stream.Stream;

class AssociationAnalysis {

  public ArrayList<AssociationResult> calculateScores(Stream<EntitySentiment> sentiments) {
    ArrayList<AssociationResult> res = new ArrayList<AssociationResult>();
    sentiments.forEach(sentiment -> updateScore(res, sentiment));
    return res;
  }

  private void updateScore(ArrayList<AssociationResult> res, EntitySentiment sentiment) {
    float scoreDiff = sentiment.getSentiment() * sentiment.getSignificance();
    for (AssociationResult association : res) {
      if (association.getContent().compareTo(sentiment.getContent()) == 0) {
	association.updateScore(association.getScore() + scoreDiff);
      }
    }
    res.add(new AssociationResult(sentiment.getContent(), scoreDiff));
  }
}
