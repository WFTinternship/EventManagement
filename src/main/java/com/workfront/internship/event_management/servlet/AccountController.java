package com.workfront.internship.event_management.servlet;


import com.google.gson.JsonObject;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.service.UserServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hermine Turshujyan 8/9/16.
 */
public class AccountController extends HttpServlet {

    private final String UPLOAD_DIRECTORY = "/Users/hermine/IdeaProjects/EventManagement/user_uploads/avatar";
    private UserService userService = new UserServiceImpl();

    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        //request type is multi-part content only in registration request
        if (!ServletFileUpload.isMultipartContent(request)) {

            String action = request.getParameter("action");

            switch (action) {
                case "LOGIN":
                    login(request, response);
                    break;
                case "LOGOUT":
                    logout(request, response);
                    break;
            }
        } else {
            register(request, response);
        }
    }


    //helper methods
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            JsonObject result = new JsonObject();
            result.addProperty("success", "Login success!");

            response.setContentType("application/json");
            response.getWriter().print(result);

        } catch (OperationFailedException e) {

            JsonObject result = new JsonObject();
            result.addProperty("error", e.getMessage());

            response.setContentType("application/json");
            response.getWriter().print(result);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();
        response.sendRedirect("index.jsp");
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //process only if its multipart content
            try {
                List<FileItem> parts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                User user = new User();

                for (FileItem item : parts) {
                    if (!item.isFormField()) {
                        // TODO: 8/14/16 rename image file
                        String fileName = new File(item.getName()).getName();
                        String avatarPath = UPLOAD_DIRECTORY + File.separator + fileName;
                        item.write(new File(avatarPath));

                        //set avatar to created user object
                        user.setAvatarPath(avatarPath);
                    } else {

                        String fieldName = item.getFieldName();
                        String fieldValue = item.getString();

                        switch (fieldName) {
                            case "firstName":
                                user.setFirstName(fieldValue);
                                break;
                            case "lastName":
                                user.setLastName(fieldValue);
                                break;
                            case "email":
                                user.setEmail(fieldValue);
                                break;
                            case "password":
                                user.setPassword(fieldValue);
                                break;
                            case "phone":
                                user.setPhoneNumber(fieldValue);
                                break;
                        }
                    }
                }

                try {
                    userService.addAccount(user);

                    JsonObject result = new JsonObject();
                    result.addProperty("success", "You are successfully registered!");

                    response.setContentType("application/json");
                    response.getWriter().print(result);

                } catch (OperationFailedException e) {

                    JsonObject result = new JsonObject();
                    result.addProperty("error", e.getMessage());

                    response.setContentType("application/json");
                    response.getWriter().print(result);
                }


            } catch (Exception ex) {
                response.sendRedirect("error.jsp");
            }
    }
}
