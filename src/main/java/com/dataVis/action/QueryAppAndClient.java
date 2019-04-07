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

@WebServlet("/appAndClient")
public class QueryAppAndClient extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        List<ZLocation> app = new dataVisdaoImpl().queryAppName();
        List<ZLocation> client = new dataVisdaoImpl().queryClient();
        List appAndClient  = new ArrayList();
        appAndClient.add(app);
        appAndClient.add(client);
        String appAndClientdata = "";

        appAndClientdata = JSON.toJSONString(appAndClient);
        System.out.println(appAndClientdata);
        OutputStream out=resp.getOutputStream();
        out.write(appAndClientdata.getBytes("UTF-8"));
        out.flush();
    }
}
