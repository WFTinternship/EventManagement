package com.workfront.internship.event_management.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Hermine Turshujyan 9/12/16.
 */
@Component
public class FileService {

    private static final Logger logger = Logger.getLogger(FileService.class);

    public String saveFile(String uploadPath, MultipartFile image) throws IOException {

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

    public boolean isValidImage(MultipartFile image) {
        return (image.getContentType().equals("image/jpeg") || image.getContentType().equals("image/png"));
    }

    public boolean isValidFile(MultipartFile file) {
        // TODO: 9/12/16 implement 
        return true;
    }


}
