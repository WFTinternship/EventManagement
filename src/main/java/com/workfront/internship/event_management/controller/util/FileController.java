package com.workfront.internship.event_management.controller.util;

import com.workfront.internship.event_management.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by hermineturshujyan on 9/20/16.
 */
@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping("/events/images/{imageName}.{suffix}")
    @ResponseBody
    public byte[] getPhoto(@PathVariable String imageName, @PathVariable String suffix, HttpServletRequest request) throws IOException {
        ServletContext context = request.getSession().getServletContext();
        String webContentRoot = context.getRealPath("/resources/uploads");

        return fileService.getEventImage(webContentRoot, imageName + "." + suffix);
    }

    @RequestMapping("/events/files/{fileName}")
    @ResponseBody
    public byte[] getFile(@PathVariable String fileName) throws IOException {
        return fileService.getEventFile(fileName);
    }
}
