package org.token.jwtservlet;

import java.io.*;
import java.util.HashMap;

import com.google.gson.Gson;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        Gson gson = new Gson();
        HashMap<String,Object> map = new HashMap<>();

        map.put("message", message);
        // Hello
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(map));
    }

    public void destroy() {
    }
}