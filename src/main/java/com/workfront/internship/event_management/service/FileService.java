package com.workfront.internship.event_management.service;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

/**
 * Created by Hermine Turshujyan 9/12/16.
 */
@Component
public class FileService {

    private static final String EVENT_FILE_DIRECTORY = "/events/files";
    private static final String EVENT_IMAGE_DIRECTORY = "/events/images";
    private static final String USER_AVATAR_DIRECTORY = "/avatars";

    private static final Logger logger = Logger.getLogger(FileService.class);

    public String saveEventImage(String rootPath, MultipartFile image) throws IOException {
        return saveFile(rootPath + EVENT_IMAGE_DIRECTORY, image);
    }

    public String saveEventFile(String rootPath, MultipartFile file) throws IOException {
        return saveFile(rootPath + EVENT_FILE_DIRECTORY, file);
    }

    public String saveAvatar(String rootPath, MultipartFile image) throws IOException {
        return saveFile(rootPath + USER_AVATAR_DIRECTORY, image);
    }

    public String saveFile(String uploadPath, MultipartFile image) throws IOException {

        // creates the directory if it does not exist
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileName = image.getOriginalFilename();
        String filePath = null;
        String uniqueFileName = null;

        if (!fileName.isEmpty()) {

            //get uploaded file extension
            String ext = fileName.substring(fileName.lastIndexOf("."));

            //generate random image name
            String uuid = UUID.randomUUID().toString();
            uniqueFileName = String.format("%s%s", uuid, ext);

            //create file path
            filePath = uploadPath + File.separator + uniqueFileName;
            File storeFile = new File(filePath);

            // saves the file on disk
            FileUtils.writeByteArrayToFile(storeFile, image.getBytes());
        }

        return uniqueFileName;
    }

    public boolean isValidImage(MultipartFile image) {
        return (image.getContentType().equals("image/jpeg") || image.getContentType().equals("image/png"));
    }

    public boolean isValidFile(MultipartFile file) {
        // TODO: 9/12/16
        return (file.getContentType().equals("application/pdf")
                || file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || file.getContentType().equals("application/msword"));
    }

    public void deleteFile(String path) throws IOException {
        FileUtils.deleteDirectory(new File(path));
    }
}
