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

/**
 * 联网方式action类
 */
@WebServlet("/network")
public class QueryNetwork extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        List<ZLocation> loc = new dataVisdaoImpl().queryNetwork();
        String networkdata = "";

        networkdata = JSON.toJSONString(loc);
        System.out.println(networkdata);
        OutputStream out=resp.getOutputStream();
        out.write(networkdata.getBytes("UTF-8"));
        out.flush();
    }
}
