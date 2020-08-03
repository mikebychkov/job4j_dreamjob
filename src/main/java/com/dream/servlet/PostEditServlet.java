package com.dream.servlet;

import com.dream.model.Candidate;
import com.dream.model.Post;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Post post = null;
        String id = req.getParameter("id");
        if (id != null) {
            post = Store.defaultStore().findPostById(
                    Integer.parseInt(id)
            );
        }
        if (post == null) {
            post = new Post(0, "");
        }
        req.setAttribute("post", post);
        req.getRequestDispatcher("post/edit.jsp").forward(req, resp);
    }
}
