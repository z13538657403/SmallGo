package com.test.zhangtao.activitytest.bean;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/10/1.
 */
public class ProductCategory implements Serializable
{
    private Long id;
    private String name;
    private Long sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }
}
