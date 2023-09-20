package com.dreams;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get请求逻辑
        System.out.println(req.getMethod());
        //数据长度
        resp.addHeader("Content-Length","12");
        //以纯文本方式展示
        resp.addHeader("Content-Type","text/plain;charset=utf-8");
        resp.getOutputStream().write("Hello World!".getBytes());
    }
}
