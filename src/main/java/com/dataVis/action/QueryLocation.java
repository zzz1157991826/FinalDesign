package com.dataVis.action;

import com.dataVis.dao.Impl.dataVisdaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.dataVis.entity.ZLocation;

@WebServlet("/location")
public class QueryLocation extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");

        List<ZLocation> loc = new dataVisdaoImpl().queryLocation();
        //System.out.println();
        String data = "";
//        for (int i = 0; i<loc.size();i++){
//            data += JSON.toJSONString(new ArrayList<Location>());
//        }
        data = JSON.toJSONString(loc);
        System.out.println(data);
        OutputStream out=resp.getOutputStream();
        out.write(data.getBytes("UTF-8"));
        out.flush();
        //System.out.println(req.getRequestURL());
    }
//    public static void main (String[] args) {
//        QueryLocation q = new QueryLocation();
//    }
}
