package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class Login extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    String logMsg = "";
    String redirectUrl = "/";

    if (!userService.isUserLoggedIn()) {
      logMsg = "user is now logged in";
      redirectUrl = userService.createLoginURL("/login");
    } else {
      logMsg = "user is already logged in";
    }
    response.getWriter().println(logMsg);
    response.sendRedirect(redirectUrl);
  }
}
