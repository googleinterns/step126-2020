package com.google.sps.data;

import java.util.ArrayList;

/** Data for top negative and positive associations  */
public final class AssociationData {

  private ArrayList<String> positive;
  private ArrayList<String> negative;
	      
  public AssociationData(ArrayList<String> positive, ArrayList<String> negative) {
    this.positive = positive;
    this.negative = negative;
  }

  public ArrayList<String> getPositive() {
    return positive;
  }

  public ArrayList<String> getNegative() {
    return negative;
  }
}
