package com.test.zhangtao.activitytest.bean;

import java.util.List;

/**
 * Created by zhangtao on 16/11/6.
 */
public class Order
{
    public static final int STATUS_SUCCESS = 1;     //支付成功的订单
    public static final int STATUS_PAY_FALL = -2;   //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0;    //待支付的订单

    private Long id;
    private String orderNum;
    private Long createdTime;
    private Float amount;
    private int status;
    private List<OrderItem> items;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
