package com.dream.servlet;

import com.dream.model.Post;
import com.dream.store.MemStore;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class PostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("posts", Store.defaultStore().findAllPosts());
        req.setAttribute("user", req.getSession().getAttribute("user"));
        req.getRequestDispatcher("posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Store.defaultStore().save(
                    new Post(
                            Integer.parseInt(req.getParameter("id")),
                            req.getParameter("name")
                    )
            );
        } catch (SQLException e) {

        }
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
