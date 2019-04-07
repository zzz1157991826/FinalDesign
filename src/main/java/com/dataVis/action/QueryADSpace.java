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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/adspace")
public class QueryADSpace extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        List<ZLocation> adspace = new dataVisdaoImpl().queryAdSpace();

        String adspacedata = "";

        adspacedata = JSON.toJSONString(adspace);
        System.out.println(adspacedata);
        OutputStream out=resp.getOutputStream();
        out.write(adspacedata.getBytes("UTF-8"));
        out.flush();
    }
}
