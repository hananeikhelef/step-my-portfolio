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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.sps.data.Message;
import com.google.sps.servlets.AuthenticationServlet;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns comments from server */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    
/** doGet Responds with all comments, serialized as json*/
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    ArrayList<Message> messagesList = new ArrayList<Message>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String content = (String) entity.getProperty("content");
      long timestamp = (long) entity.getProperty("timestamp");
      String userEmail = (String) entity.getProperty("userEmail");

      Message message = new Message(id, content, timestamp, userEmail);
      messagesList.add(message);
    }

    Gson gson = new Gson();
    String json = gson.toJson(messagesList);

    response.setContentType("application/json");
    response.getWriter().println(json);

  }

   /** doPost adds new messages to server data and stores it*/
   @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String content = request.getParameter("content");
      long timestamp = System.currentTimeMillis();
      String email = userService.getCurrentUser().getEmail();


      Entity messageEntity = new Entity("Message");
      messageEntity.setProperty("content", content);
      messageEntity.setProperty("timestamp", timestamp);
      messageEntity.setProperty("userEmail", email);

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(messageEntity);
      response.sendRedirect("/contact.html");


    } else {
      response.sendRedirect("/contact.html");
      return;
    }
  }
}
