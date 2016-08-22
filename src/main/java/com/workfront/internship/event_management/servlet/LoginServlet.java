package com.workfront.internship.event_management.servlet;

import com.google.gson.JsonObject;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.EventManagementApplication;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Hermine Turshujyan 8/15/16.
 */
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init();
        userService = EventManagementApplication.getApplicationContext(servletConfig.getServletContext()).getBean(UserService.class);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        JsonObject result = new JsonObject();
        try {

            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            result.addProperty("success", "Login success!");
        } catch (OperationFailedException e) {
            result.addProperty("error", e.getMessage());
        } finally {
            response.setContentType("application/json");
            response.getWriter().print(result);
        }
    }


}
