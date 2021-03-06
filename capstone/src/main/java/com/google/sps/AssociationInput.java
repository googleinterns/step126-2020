package com.google.sps;

import java.util.ArrayList;

/** Stores the original open text response and scopes within it is valid */
public class AssociationInput {

  private String message;
  private ArrayList<String> scopes;

  /**
   * Creates a new AssociationInput object
   *
   * @param message the open text response
   * @param scopes the precincts/scopes within which the response is applicable
   */
  public AssociationInput(String message, ArrayList<String> scopes) {
    this.message = message;
    this.scopes = scopes;
  }

  /** @return the open text response */
  public String getMessage() {
    return message;
  }

  /** @return the precincts/scopes within which the response is applicable */
  public ArrayList<String> getScopes() {
    return scopes;
  }

  public String toString() {
    return "{" + message + ", " + scopes.toString() + "}";
  }

  public boolean equals(Object obj) {
    if (obj == null || !AssociationInput.class.isAssignableFrom(obj.getClass())) {
      return false;
    }
    AssociationInput x = (AssociationInput) obj;
    return x.getMessage().equals(message) && x.getScopes().equals(scopes);
  }
}
