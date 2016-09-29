package com.workfront.internship.event_management.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermineturshujyan on 9/29/16.
 */
public class FileServiceUnitTest {

    private static FileService fileService;

    @BeforeClass
    public static void setUpClass() {
        fileService = new FileService();
    }

    @AfterClass
    public static void tearDownClass() {
        fileService = new FileService();
    }

//    @Test
//    public String saveEventImage(String rootPath, MultipartFile image) throws IOException {
//        saveEventImage
//    }
//
//    @Test
//
//    public String saveEventFile(String rootPath, MultipartFile file) throws IOException {
//    }
//    @Test
//
//    public String saveAvatar(String rootPath, MultipartFile image) throws IOException {
//    }
//    @Test
//
//    public List<String> saveEventPhotos(String rootPath, MultipartFile[] images, int eventId, int userId) throws IOException {
//
//    }
}
