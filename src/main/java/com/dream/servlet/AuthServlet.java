package com.dream.servlet;

import com.dream.model.User;
import com.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String log = req.getParameter("log");
        if ("0".equals(log)) {
            req.getSession().setAttribute("user", new User());
        }
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        User user = Store.defaultStore().findUser(email);
        if (user != null && password.equals(user.getPassword())) {
        /*if ("root@local".equals(email) && "root".equals(password)) {
            User user = getAdmin();*/
            HttpSession sc = req.getSession();
            sc.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("error", "Неверный email или пароль");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    private User getAdmin() {
        User admin = new User();
        admin.setId(1);
        admin.setName("Admin");
        admin.setEmail("root@local");
        admin.setPassword("root");
        return admin;
    }
}
