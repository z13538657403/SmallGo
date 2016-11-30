package com.test.zhangtao.activitytest.bean;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/11/7.
 */
public class Favorites implements Serializable
{
    private Long id;
    private Long createTime;
    private Wares wares;

    public Favorites(){}

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wares getWares() {
        return wares;
    }

    public void setWares(Wares wares) {
        this.wares = wares;
    }
}
