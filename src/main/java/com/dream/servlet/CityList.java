package com.dream.servlet;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CityList extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, String> cities = new HashMap<>();
        cities.put("msk", "Moskov");
        cities.put("spb", "SPB");
        cities.put("tas", "Tashkent");

        JSONObject jo = new JSONObject(cities);

        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.println(jo.toString());
        writer.flush();
    }
}
