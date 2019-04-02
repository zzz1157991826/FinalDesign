package com.dataVis.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class ZLocation {

    @JSONField(name = "value")
    private Integer name;//请求数
    @JSONField(name = "name")
    private String value;//省份

    public ZLocation(Integer name, String value) {
        this.name = name;
        this.value = value;
    }
    public ZLocation() {
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
