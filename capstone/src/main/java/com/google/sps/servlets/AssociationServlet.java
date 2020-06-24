// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.AssociationData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to fetch association data with most positive and negative associations */
@WebServlet("/associations")
public class AssociationServlet extends HttpServlet {

  private static final String OUTPUT_TYPE = "applications/json;";
  private static final ArrayList<String> positive = new ArrayList(Arrays.asList("test 1", "test 2", "test 3"));
  private static final ArrayList<String> negative = new ArrayList(Arrays.asList("yeet 1", "yeet 2", "yeet 3"));

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType(OUTPUT_TYPE);

    AssociationData output = new AssociationData(positive, negative);
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(output));
  }
}
