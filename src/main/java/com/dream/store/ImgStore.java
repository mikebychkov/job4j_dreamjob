package com.dream.store;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImgStore {

    public static File folder() {
        File folder = new File(folderName());
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    public static String folderName() {
        return "dreamjob/images";
    }

    public static List<String> uploadPhotos(HttpServletRequest req, HttpServlet servlet) {
        List<String> rsl = new ArrayList<>();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = servlet.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = ImgStore.folder();
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    File file = new File(folder + File.separator + fileName);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                        rsl.add(fileName);
                    }
                } else {
                    System.out.println(item.getFieldName() + " : " + item.getString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
