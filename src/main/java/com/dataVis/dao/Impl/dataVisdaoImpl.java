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

    public List<ZLocation> queryIsp() {
        List<ZLocation> isp = new ArrayList<ZLocation>();
        String sql = "SELECT ispname,ispct\n" +
                "FROM isp_ct\n" +
                "GROUP BY ispname ;";
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
                loc.setName(rs.getInt("ispct"));
                loc.setValue(rs.getString("ispname"));
                isp.add(loc);
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

        return isp;
    }

    public List<ZLocation> queryNetwork() {
        List<ZLocation> network = new ArrayList<ZLocation>();
        String sql = "SELECT networkmannername ,networkct\n" +
                "FROM network_ct\n" +
                "GROUP BY networkmannername;";
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
                loc.setName(rs.getInt("networkct"));
                loc.setValue(rs.getString("networkmannername"));
                network.add(loc);
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

        return network;
    }

    public List<ZLocation> queryAppName() {
        List<ZLocation> appname = new ArrayList<ZLocation>();
        String sql = "SELECT appname,appct\n" +
                "FROM appname_ct\n" +
                "WHERE appct > 3000\n" +
                "GROUP BY appname;";
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
                loc.setName(rs.getInt("appct"));
                loc.setValue(rs.getString("appname"));
                appname.add(loc);
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

        return appname;
    }

    public List<ZLocation> queryClient() {
        List<ZLocation> client = new ArrayList<ZLocation>();
        String sql = "SELECT client,clientct\n" +
                "FROM client_ct\n" +
                ";";
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
                loc.setName(rs.getInt("clientct"));
                loc.setValue(rs.getString("client"));
                client.add(loc);
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

        return client;
    }

    public List<ZLocation> queryAdSpace() {
        List<ZLocation> adspace = new ArrayList<ZLocation>();
        String sql = "SELECT adspacetypename,adspacect\n" +
                "FROM adspace_ct;";
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
                loc.setName(rs.getInt("adspacect"));
                loc.setValue(rs.getString("adspacetypename"));
                adspace.add(loc);
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

        return adspace;
    }

    public static void main (String[] args) {
        List<ZLocation> loc = new dataVisdaoImpl().queryAdSpace();
        System.out.println(loc.get(1).getValue()+":"+loc.get(1).getName());
    }
}
