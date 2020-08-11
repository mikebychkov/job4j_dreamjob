package com.dream.servlet.ajax;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GreetingServletJSON extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        //resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With");
        //resp.setHeader("Access-Control-Allow-Methods", "GET");
        //resp.setHeader("Access-Control-Max-Age", "1728000");

        String name = req.getParameter("name");

        Map<String, String> map = new HashMap<>();
        map.put("msg", "Nice to meet you, " + name);

        JSONObject jo = new JSONObject(map);

        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(jo.toString());
        writer.flush();
    }
}