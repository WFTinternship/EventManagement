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

    private final String UPLOAD_DIRECTORY = "/";

    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

      /*  switch (action) {
            case "LOGIN":
                login(request, response);
                break;
            case "LOGOUT":
                logout(request, response);
                break;
            case "REGISTER":*/
                register(request);

        /*        break;
        } */
    }

    //helper methods
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserService userService = new UserServiceImpl();

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

    private void register(HttpServletRequest request) {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        //process only if its multipart content
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        String name = new File(item.getName()).getName();
                        item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
                    }
                }

                //File uploaded successfully
                request.setAttribute("message", "File Uploaded Successfully");
            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            }

        } else {
            request.setAttribute("message",
                    "Sorry this Servlet only handles file upload request");
        }

        // request.getRequestDispatcher("/result.jsp").forward(request, response);


    }
}
