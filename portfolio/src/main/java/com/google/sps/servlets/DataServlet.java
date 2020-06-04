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
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Message;

/** Servlet that returns comments from server */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    
    private ArrayList<String> messages = new ArrayList<>();

/** init add hard coded messages */
  @Override
  public void init() {
    messages.add("Good content but i don't like the design");
    messages.add("Design is bad");
    messages.add("It is much easier to apologise than it is to get permission. - Grace Hopper");
  }

/** doGet Responds with all comments, serialized as json*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Query query = new Query("messageEntity").addSort("timestamp", SortDirection.DESCENDING);


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Message> messagesList = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String content = (String) entity.getProperty("content");
      long timestamp = (long) entity.getProperty("timestamp");

      Message message = new Message(id, content, timestamp);
      messagesList.add(message);
    }

    // Gson gson = new Gson();

    // response.setContentType("application/json;");
    // response.getWriter().println(gson.toJson(tasks));

    Gson gson = new Gson();
    String json = gson.toJson(messagesList);
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(json));

  }

   /** doPost adds new messages to server data*/
   @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String value = request.getParameter("message");
      long timestamp = System.currentTimeMillis();

      Entity messageEntity = new Entity("messageEntity");
      messageEntity.setProperty("title", content);
      messageEntity.setProperty("timestamp", timestamp);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      
      datastore.put(messageEntity);


      messages.add(value);

    // Redirect back to the contact page.
    response.sendRedirect("/contact.html");
  }
}
