package com.workfront.internship.event_management.servlet;


import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Hermine Turshujyan 8/9/16.
 */
public class AccountController extends HttpServlet {

    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        switch (action) {
            case "LOGIN":
                login(request, session);
                break;
            case "LOGOUT":
                logout(response, session);
                break;
            case "REGISTER":
                register(request);
                break;
        }
    }

    //helper methods
    private void login(HttpServletRequest request, HttpSession session) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserService userService = new UserServiceImpl();
        User user = null;
        //try {
        user = userService.login(email, password);
        session.setAttribute("user", user);
           /* }catch(OperationFailedException e){
                request.setAttribute("error_message", e.getMessage());
            }*/

        // rd.forward(request, response);
    }

    private void logout(HttpServletResponse response, HttpSession session) throws IOException {
        session.setAttribute("user", null);
        session.invalidate();
        response.sendRedirect("index.jsp");
    }

    private void register(HttpServletRequest request) {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


    }
}
