package com.test.zhangtao.activitytest.bean;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/9/26.
 */
public class BaseBean implements Serializable
{
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
