package com.google.sps;

import java.util.ArrayList;

public class AssociationInput {

  private String message;
  private ArrayList<String> scopes;

  public AssociationInput(String message, ArrayList<String> scopes) {
    this.message = message;
    this.scopes = scopes;
  }

  public String getMessage() {
    return message;
  }

  public ArrayList<String> getScopes() {
    return scopes;
  }
}
