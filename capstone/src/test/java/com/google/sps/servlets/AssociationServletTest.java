package com.google.sps.servlet;

import static org.junit.Assert.assertEquals;

import com.google.gson.Gson;
import com.google.sps.data.AssociationData;
import com.google.sps.servlets.AssociationServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.mockito.Mockito;

public class AssociationServletTest extends Mockito {

  @Test
  public void testServlet() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new AssociationServlet().doGet(request, response);
    writer.flush();

    Gson gson = new Gson();
    ArrayList<String> positiveExpected = new ArrayList(Arrays.asList("hi", "test1", "test2"));
    ArrayList<String> negativeExpected = new ArrayList(Arrays.asList("yeet", "hi", "think"));
    AssociationData res = gson.fromJson(stringWriter.toString(), AssociationData.class);
    assertEquals(res.getPositive(), positiveExpected);
    assertEquals(res.getNegative(), negativeExpected);
  }
}
