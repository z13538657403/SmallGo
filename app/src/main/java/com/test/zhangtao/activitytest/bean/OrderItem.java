package com.test.zhangtao.activitytest.bean;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/11/6.
 */
public class OrderItem implements Serializable
{
    private Long id;
    private Float amount;
    private Wares wares;

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
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
