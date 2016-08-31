package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.UserService;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.workfront.internship.event_management.controller.util.PageParameters.*;

/**
 * Created by Hermine Turshujyan 8/22/16.
 */
@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class);

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
    public CustomResponse register(HttpServletRequest request,
                                   @RequestParam(value = "avatar", required = false) MultipartFile image) {

        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);

        CustomResponse result = new CustomResponse();

        if (!isMultipartContent) {
            String message = "Invalid request";
            logger.error(message);

            result.setStatus(ACTION_FAIL);
            result.setMessage(message);
            return result;
        }

        User user = new User();

        //save uploaded file if avatar is uploaded
        if (!image.isEmpty()) {
            try {
                ServletContext servletContext = request.getSession().getServletContext();
                String uploadPath = servletContext.getRealPath("") + UPLOAD_DIRECTORY;

                String avatarPath = saveFile(uploadPath, image);

                //save avatar path to user obj
                user.setAvatarPath(avatarPath);
            } catch (IOException e) {
                result.setStatus(ACTION_FAIL);
                result.setMessage("Unable to upload a file!");
                return result;
            }
        }

        //set request parameters to user
        setRequestParametersToUser(user, request);

        try {
            //insert user into db
            userService.addAccount(user);

            result.setStatus(ACTION_SUCCESS);
            result.setMessage("You are successfully registered!");
        } catch (OperationFailedException e) {
            result.setStatus(ACTION_FAIL);
            result.setMessage(e.getMessage());
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

    //helper methods
    private void setRequestParametersToUser(User user, HttpServletRequest request) {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        user.setFirstName(firstName)
                .setLastName(lastName)
                .setPassword(password)
                .setPhoneNumber(phone);
    }


    private void validateImage(MultipartFile image) {
        if (!image.getContentType().equals("image/jpeg")) {
            throw new RuntimeException("Only JPG images are accepted");
        }
    }

    private String saveFile(String uploadPath, MultipartFile image) throws IOException {

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileName = image.getOriginalFilename();
        String filePath = null;

        if (!fileName.isEmpty()) {

            //get uploaded file extension
            String ext = fileName.substring(fileName.lastIndexOf("."));

            //generate random image name
            String uuid = UUID.randomUUID().toString();
            String uniqueFileName = String.format("%s.%s", uuid, ext);

            //create file path
            filePath = uploadPath + File.separator + uniqueFileName;
            File storeFile = new File(filePath);

            // saves the file on disk
            FileUtils.writeByteArrayToFile(storeFile, image.getBytes());
        }

        return filePath;
    }

//    private void saveImage(String filename, ) {
//        try {
//            File file = new File(servletContext.getRealPath("/") + "/"
//                    + filename);
//
//            System.out.println("Go to the location:  " + file.toString()
//                    + " on your computer and verify that the image has been stored.");
//        } catch (IOException e) {
//            throw e;
//        }
//    }

}
