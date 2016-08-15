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
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hermine Turshujyan 8/15/16.
 */
public class RegistrationServlet extends HttpServlet {

    private final String UPLOAD_DIRECTORY = "/Users/hermine/IdeaProjects/EventManagement/user_uploads/avatar";

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        if (ServletFileUpload.isMultipartContent(request)) {

            try {
                List<FileItem> parts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(request);

                User user = new User();
                String avatarPath;

                for (FileItem item : parts) {
                    if (!item.isFormField()) {

                        // TODO: 8/14/16 rename image file
                        if (!item.getName().isEmpty()) {

                            String fileName = new File(item.getName()).getName();

                            avatarPath = UPLOAD_DIRECTORY + File.separator + fileName;

                            item.write(new File(avatarPath));
                        }
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

                //rename uploaded file
                //  String uploadedFileName
                //String ext = File
                //String fileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), ext);

                //    new File(fileName);
                //set avatar to created user object
                //     user.setAvatarPath(avatarPath);


                JsonObject result = new JsonObject();

                try {
                    UserService userService = new UserServiceImpl();
                    userService.addAccount(user);

                    result.addProperty("success", "You are successfully registered!");
                } catch (OperationFailedException e) {
                    result.addProperty("error", e.getMessage());
                } finally {
                    response.setContentType("application/json");
                    response.getWriter().print(result);
                }

            } catch (Exception ex) {
                response.sendRedirect("error.jsp");
            }
        }
    }
}
