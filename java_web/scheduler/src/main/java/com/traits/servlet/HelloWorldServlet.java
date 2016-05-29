package com.traits.servlet;

/**
 * Created by YeFeng on 2016/5/12.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet
{
    @SuppressWarnings("deprecation")
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {

        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

        out.println("<html><head><title>Hello World Sample</title></head>");
        out.println("<body><h1>Hello World Title<h1><h2>" +new Date().toLocaleString() + "</h2></body></html>");
        out.flush();

    }

}
