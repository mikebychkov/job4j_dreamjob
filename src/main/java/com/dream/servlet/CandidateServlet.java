package com.dream.servlet;

import com.dream.model.Candidate;
import com.dream.store.MemStore;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", Store.defaultStore().findAllCandidates());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Store.defaultStore().save(
                    new Candidate(
                            Integer.parseInt(req.getParameter("id")),
                            req.getParameter("name")
                    )
            );
        } catch (SQLException e) {

        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
