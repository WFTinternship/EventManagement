package com.workfront.internship.event_management.servlet;

import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.EventManagementApplication;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Hermine Turshujyan 8/16/16.
 */
public class CheckEmailServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init();
        userService = EventManagementApplication.getApplicationContext(servletConfig.getServletContext()).getBean(UserService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            response.getWriter().print("false");
        } else {
            response.getWriter().print("true");
        }

    }
}
