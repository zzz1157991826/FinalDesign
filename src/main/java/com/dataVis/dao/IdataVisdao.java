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
}
