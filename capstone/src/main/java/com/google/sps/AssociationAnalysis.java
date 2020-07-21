package com.google.sps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import opennlp.tools.stemmer.PorterStemmer;

/** Calculates association scores from a list of sentiments per entity mention */
public class AssociationAnalysis {

  HashMap<String, AssociationResult> res;
  PorterStemmer stemmer;

  public AssociationAnalysis() {
    res = new HashMap<String, AssociationResult>();
    stemmer = new PorterStemmer();
  }

  public AssociationAnalysis(ArrayList<AssociationResult> initialResults) {
    res = new HashMap<String, AssociationResult>();
    stemmer = new PorterStemmer();
    for (AssociationResult association : initialResults) {
      res.put(stemmer.stem(association.getNormalizedString()), association);
    }
  }

  /**
   * Calculates association scores for the input array list of mentions
   *
   * @param sentiments array list of sentiments of the mentions
   * @return the array list of association scores sorted by score
   */
  public ArrayList<AssociationResult> calculateScores(ArrayList<EntitySentiment> sentiments) {
    sentiments.forEach(sentiment -> updateScore(res, sentiment));
    ArrayList<AssociationResult> list = new ArrayList(res.values());
    Collections.sort(list, AssociationResult.ORDER_BY_SCORE);
    return list;
  }

  /** Updates the result array with the new entity mention given */
  private void updateScore(HashMap<String, AssociationResult> res, EntitySentiment sentiment) {
    AssociationResult association = res.get(sentiment.getKey());
    if (association != null) {
      association.updateResult(sentiment);
    } else {
      res.put(stemmer.stem(sentiment.getKey()), new AssociationResult(sentiment));
    }
  }
}
