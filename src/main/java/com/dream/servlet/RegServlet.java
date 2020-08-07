package com.dream.servlet;

import com.dream.model.User;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("reg.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = new User(0, name);
        user.setEmail(email);
        user.setPassword(password);

        try {
            Store.defaultStore().saveUser(user);
            resp.sendRedirect(req.getContextPath() + "/auth.do");
        } catch (SQLException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
