package com.google.sps;

import opennlp.tools.stemmer.PorterStemmer;

/** Generates lowercase no spaces word stems for keys in the AssociationResults hashmap */
public class AssociationKey {

  private static PorterStemmer stemmer;

  public AssociationKey() {
    stemmer = new PorterStemmer();
  }

  /**
   * @param content the word/phrase that makes up an association
   * @return the lowercase root for use as a key in a hashmap
   */
  public String getKey(String content) {
    return stemmer.stem(content.toLowerCase().trim());
  }

  /**
   * @param sentiment an entity sentiment to generate a key from
   * @return the lowercase root of the content for use as a key in a hashmap
   */
  public String getKey(EntitySentiment sentiment) {
    return getKey(sentiment.getContent());
  }

  /**
   * @param result the association result to generate a key from
   * @return the lowercase root of the content for use as a key in a hashmap
   */
  public String getKey(AssociationResult result) {
    return getKey(result.getContent());
  }
}
