package com.workfront.internship.event_management.servlet;

import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Hermine Turshujyan 8/16/16.
 */
public class CheckEmailServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");

        UserService userService = new UserServiceImpl();

        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            response.getWriter().print("false");
        } else {
            response.getWriter().print("true");
        }

    }
}
