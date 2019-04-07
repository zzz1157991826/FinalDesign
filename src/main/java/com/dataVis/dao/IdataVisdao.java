package com.dataVis.dao;

import com.dataVis.entity.ZLocation;

import java.util.List;

public interface IdataVisdao {

    //查询地域情况
    public List<ZLocation> queryLocation();
    //查询运营商情况
    public List<ZLocation> queryIsp();
    //查询运营商情况
    public List<ZLocation> queryNetwork();
    //查询用户使用的app情况
    public List<ZLocation> queryAppName();
    //查询用户使用的设备情况
    public List<ZLocation> queryClient();
    //查询用户使用的设备情况
    public List<ZLocation> queryAdSpace();

}
