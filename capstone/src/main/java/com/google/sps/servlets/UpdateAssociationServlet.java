package com.google.sps;

import com.google.sps.AssociationResult;
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
    ArrayList<AssociationResult> res = association.calculateScores(CloudNLPAssociation.analyzeAssociations(getComments()));
    response.getWriter().println(res);
  }

  private ArrayList<String> getComments() {
    return new ArrayList(Arrays.asList("the police keep on pulling me over for no good reason and it frustrates me", 
			    "I don't understand why the police have to be so militarized and it scares me",
			    "the police need their funding cut and it should be redistributed to other social services",
			    "i haven't had any bad experiences with the police personally"));
  }
}
