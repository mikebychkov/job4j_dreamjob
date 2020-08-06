package com.dream.servlet;

import com.dream.model.Candidate;
import com.dream.store.ImgStore;
import com.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class PhotoUpload extends HttpServlet {

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
        List<String> files = ImgStore.uploadPhotos(req, this);
        //
        String id = req.getParameter("id");
        Candidate cand = getCandidate(id);
        //
        if (files.size() > 0 && cand.getId() != 0) {
            Store.defaultStore().savePhoto(cand, files.get(0));
        }
        req.setAttribute("id", Integer.parseInt(id));
        doGet(req, resp);
    }
}
