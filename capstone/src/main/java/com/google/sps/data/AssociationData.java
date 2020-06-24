package com.google.sps.data;

import java.util.ArrayList;

/** Data for top negative and positive associations  */
public final class AssociationData {

  private ArrayList<String> positive;
  private ArrayList<String> negative;
	      
  public AssociationData(ArrayList<String> positiveAssociations, ArrayList<String> negativeAssociations) {
    this.positive = positiveAssociations;
    this.negative = negativeAssociations;
  }
}
