package com.google.sps.servlets;

<<<<<<< HEAD:capstone/src/main/java/com/google/sps/servlets/AssociationServlet.java
import com.google.gson.Gson;
import com.google.sps.data.AssociationData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/** Servlet to fetch association data with most positive and negative associations */
@WebServlet("/associations")
public class AssociationServlet extends HttpServlet {

  private static final String OUTPUT_TYPE = "applications/json;";
  private final ArrayList<String> positive = new ArrayList(Arrays.asList("hi", "test1", "test2"));
  private final ArrayList<String> negative = new ArrayList(Arrays.asList("yeet", "hi", "think"));

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(OUTPUT_TYPE);

    AssociationData output = new AssociationData(positive, negative);
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(output));
  }
}
