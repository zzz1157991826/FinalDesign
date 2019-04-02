package com.dataVis.dao.Impl;

import com.dataVis.dao.IdataVisdao;
import com.dataVis.entity.ZLocation;
import com.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dataVisdaoImpl implements IdataVisdao {
    public List<ZLocation> queryLocation() {
        List<ZLocation> location = new ArrayList<ZLocation>();
        String sql = "SELECT  provincename,SUM(ysrequest) ys\n" +
                "FROM rpt_ll\n" +
                "GROUP BY provincename;";
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        DbUtil dbUtil=new DbUtil();

        try {
            con=dbUtil.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();

            while(rs.next()){
                ZLocation loc = new ZLocation();
                loc.setName(rs.getInt("ys"));
                loc.setValue(rs.getString("provincename"));
                location.add(loc);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {

            try {
                if(rs!=null)rs.close();
                if(ps!=null)ps.close();
                if(con!=null&&!con.isClosed())con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return location;
    }

    public static void main (String[] args) {
        List<ZLocation> loc = new dataVisdaoImpl().queryLocation();
        System.out.println(loc.toString());
    }
}
