package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {
    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse login(Model model, HttpServletRequest request) {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        //JsonObject result = new JsonObject();
        JsonResponse result = new JsonResponse();

        try {
            User user = userService.login(email, password);

            //Save user object in session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            result.setStatus("SUCCESS");
        } catch (OperationFailedException e) {
            result.setStatus("FAIL");
            result.setMessage(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        session.invalidate();

        return "forward:/home";
    }

    @RequestMapping(value = "/registration")
    public String registration() {
        return "registration";
    }

    @RequestMapping(value = "/register", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse register(Model model, HttpServletRequest request) throws Exception {

        JsonResponse result = new JsonResponse();

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

                try {
                    userService.addAccount(user);

                    result.setStatus("SUCCESS");
                    result.setMessage("You are successfully registered!");
                } catch (OperationFailedException e) {
                    result.setStatus("FAIL");
                    result.setMessage(e.getMessage());
                } finally {
                    //  response.setContentType("application/json");
                    //  response.getWriter().print(result);
                }
            }

//            } catch (Exception ex) {
//                response.sendRedirect("error.jsp");
//            }
        }
        return result;
    }

    @RequestMapping(value = "/check-email", method = RequestMethod.POST)
    @ResponseBody
    public String checkEmail(Model model, HttpServletRequest request) {

        JsonResponse result = new JsonResponse();

        String email = request.getParameter("email");
        User user = userService.getUserByEmail(email);

        if (user != null) {
            //already registered
            return "false";
        } else {
            return "true";
        }
    }
}
