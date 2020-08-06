package com.dream.servlet;

import com.dream.model.Candidate;
import com.dream.store.ImgStore;
import com.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
 */

@MultipartConfig
public class PhotoUpload2 extends HttpServlet {

    private Candidate getCandidate(String id) {
        Candidate cand = null;
        if (id != null) {
            cand = Store.defaultStore().findCandidateById(
                    Integer.parseInt(id)
            );
        }
        if (cand == null) {
            cand = new Candidate(0, "");
        }
        return cand;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Candidate cand = getCandidate(id);
        req.setAttribute("candidate", cand);
        RequestDispatcher dispatcher = req.getRequestDispatcher("upload/photo.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("abra : " + req.getParameter("abra"));
        //
        Part filePart = req.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        //
        File folder = ImgStore.folder();
        File file = new File(folder + File.separator + fileName);
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(filePart.getInputStream().readAllBytes());
        }
        //
        String id = req.getParameter("id");
        Candidate cand = getCandidate(id);
        //
        if (cand.getId() != 0) {
            Store.defaultStore().savePhoto(cand, fileName);
        }
        req.setAttribute("id", Integer.parseInt(id));
        doGet(req, resp);
    }
}
