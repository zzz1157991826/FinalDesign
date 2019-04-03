package com.dataVis.action;

import com.alibaba.fastjson.JSON;
import com.dataVis.dao.Impl.dataVisdaoImpl;
import com.dataVis.entity.ZLocation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@WebServlet("/isp")
public class QueryIsp extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        List<ZLocation> loc = new dataVisdaoImpl().queryIsp();
        String ispdata = "";

        ispdata = JSON.toJSONString(loc);
        System.out.println(ispdata);
        OutputStream out=resp.getOutputStream();
        out.write(ispdata.getBytes("UTF-8"));
        out.flush();
    }
}
