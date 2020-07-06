package com.google.sps;

import com.google.cloud.language.v1.LanguageServiceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/update-associations")
public class UpdateAssociationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    AssociationAnalysis association = new AssociationAnalysis();
    CloudNLPAssociation nlp = new CloudNLPAssociation(LanguageServiceClient.create());
    ArrayList<AssociationResult> res =
        association.calculateScores(nlp.analyzeAssociations(getComments()));
    response.getWriter().println(res);
    nlp.close();
  }

  private ArrayList<String> getComments() {
    return new ArrayList(
        Arrays.asList(
            "the police keep on pulling me over and it frustrates me",
            "I don't understand why the police have to be so militarized",
            "funding should be redistributed to other social services",
            "i haven't had any bad experiences with the police personally"));
  }
}
