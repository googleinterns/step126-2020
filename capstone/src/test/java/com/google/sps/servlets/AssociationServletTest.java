package com.google.sps.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.google.gson.Gson;
import com.google.sps.servlets.AssociationServlet;
import com.google.sps.data.AssociationData;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import javax.servlet.http.*;
import org.junit.Test;
import org.mockito.Mockito;

public class AssociationServletTest extends Mockito{

  @Test
  public void testServlet() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);       
    HttpServletResponse response = mock(HttpServletResponse.class);    

    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);

    new AssociationServlet().doGet(request, response);

    writer.flush(); // it may not have been flushed yet...

    Gson gson = new Gson();
    ArrayList<String> positiveExpected = new ArrayList(Arrays.asList("hi", "test1", "test2"));
    ArrayList<String> negativeExpected = new ArrayList(Arrays.asList("yeet", "hi", "think"));
    AssociationData res = gson.fromJson(stringWriter.toString(), AssociationData.class);
    assertEquals(res.getPositive(), positiveExpected);
    assertEquals(res.getNegative(), negativeExpected);
  }
}
