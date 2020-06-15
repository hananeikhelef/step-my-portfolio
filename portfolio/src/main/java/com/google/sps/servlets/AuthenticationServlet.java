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
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;


@WebServlet("/authentication")
public class AuthenticationServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail(); // get user's email
      String urlToRedirectToAfterUserLogsOut = "/contact.html";
      String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut); // redirect to contact page

      String[] array = {logoutUrl, userEmail};

      response.setStatus(200); // request recived 
      String json = new Gson().toJson(array);

      response.setContentType("application/json");
      response.getWriter().println(json);

    } 
    if (!userService.isUserLoggedIn()) {
      String urlToRedirectToAfterUserLogsIn = "/contact.html";
      String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      String[] array = {loginUrl};

      response.setStatus(403); //401 Unauthorized
      
      Gson gson = new Gson();
      String json = gson.toJson(array);

      response.setContentType("application/json");
      response.getWriter().println(json);


    }
  }
}
