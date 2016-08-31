package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.workfront.internship.event_management.controller.util.PageParameters.*;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {
    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse login(HttpServletRequest request) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        CustomResponse result = new CustomResponse();

        try {
            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            result.setStatus(ACTION_SUCCESS);
        } catch (InvalidObjectException | OperationFailedException e) {
            result.setStatus(ACTION_FAIL);
            result.setMessage(e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();

        return "forward:/home";
    }

    @RequestMapping(value = "/registration")
    public String goToRegistrationPage() {
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CustomResponse register(HttpServletRequest request) throws Exception {

        CustomResponse result = new CustomResponse();

        if (ServletFileUpload.isMultipartContent(request)) {

            DiskFileItemFactory factory = new DiskFileItemFactory();

            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

            ServletFileUpload upload = new ServletFileUpload(factory);

            // constructs the directory path to store upload file
            // this path is relative to application's directory
            ServletContext servletContext = request.getSession().getServletContext();
            String uploadPath = servletContext.getRealPath("") + UPLOAD_DIRECTORY;

            // creates the directory if it does not exist
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // try {
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

                try {
                    userService.addAccount(user);

                    result.setStatus(ACTION_SUCCESS);
                    result.setMessage("You are successfully registered!");
                } catch (OperationFailedException e) {
                    result.setStatus(ACTION_FAIL);
                    result.setMessage(e.getMessage());
                }
            }
        }
        return result;
    }

    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    @ResponseBody
    public String isEmailFree(HttpServletRequest request) {

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            return RESPONSE_FALSE;
        } else {
            return RESPONSE_TRUE;
        }
    }
}
