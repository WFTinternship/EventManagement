package com.workfront.internship.event_management.servlet;

import com.google.gson.JsonObject;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.EventManagementApplication;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hermine Turshujyan 8/15/16.
 */
public class RegistrationServlet extends HttpServlet {

    @Autowired
    ServletContext servletContext;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = EventManagementApplication.getApplicationContext(getServletContext()).getBean(UserService.class);
        // userService = servletContext.getBean(UserService.class);
    }

    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        if (ServletFileUpload.isMultipartContent(request)) {

            DiskFileItemFactory factory = new DiskFileItemFactory();

            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);

            // constructs the directory path to store upload file
            // this path is relative to application's directory
            String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;

            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            try {
                List<FileItem> formItems = upload.parseRequest(request);

                User user = new User();

                if (formItems != null && formItems.size() > 0) {

                    // iterates over form's fields
                    for (FileItem item : formItems) {
                        if (!item.isFormField()) {

                            String fileName = new File(item.getName()).getName();

                            if (!fileName.isEmpty()) {
                                //get uploaded file extension
                                String ext = fileName.substring(fileName.lastIndexOf("."));
                                //  String ext1 = FilenameUtils.getExtension("/path/to/file/foo.txt");

                                // String[] parts = fileName.split(Pattern.quote("."));
                                //String ext = parts[parts.length - 1];

                                //generate random image name
                                String uuid = UUID.randomUUID().toString();
                                String uniqueFileName = String.format("%s.%s", uuid, ext);

                                //create file path
                                String filePath = uploadPath + File.separator + uniqueFileName;
                                File storeFile = new File(filePath);

                                // saves the file on disk
                                item.write(storeFile);

                                //save avatar path to user obj
                                user.setAvatarPath(filePath);
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

                    JsonObject result = new JsonObject();

                    try {
                        userService.addAccount(user);

                        result.addProperty("success", "You are successfully registered!");
                    } catch (OperationFailedException e) {
                        result.addProperty("error", e.getMessage());
                    } finally {
                        response.setContentType("application/json");
                        response.getWriter().print(result);
                    }
                }

            } catch (Exception ex) {
                response.sendRedirect("error.jsp");
            }
        }
    }

}
