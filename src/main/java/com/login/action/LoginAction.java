package com.login.action;

import com.alibaba.fastjson.JSON;
import com.login.dao.Impl.LogindaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/login")
public class LoginAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        String username=req.getParameter("username");
        String password=req.getParameter("password");

        LogindaoImpl logindao = new LogindaoImpl();

        String phoneNumber = logindao.login(username,password);

        String resultJson= JSON.toJSONString(phoneNumber);
        OutputStream out=resp.getOutputStream();
        out.write(resultJson.getBytes("UTF-8"));
        out.flush();
    }
}
