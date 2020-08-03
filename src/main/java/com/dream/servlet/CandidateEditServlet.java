package com.dream.servlet;

import com.dream.model.Candidate;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CandidateEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Candidate cand = null;
        String id = req.getParameter("id");
        if (id != null) {
            cand = Store.defaultStore().findCandidateById(
                    Integer.parseInt(id)
            );
        }
        if (cand == null) {
            cand = new Candidate(0, "");
        }
        req.setAttribute("candidate", cand);
        req.getRequestDispatcher("candidate/edit.jsp").forward(req, resp);
    }
}
