package com.test.zhangtao.activitytest.msg;

import com.test.zhangtao.activitytest.bean.Charge;

/**
 * Created by zhangtao on 16/11/4.
 */
public class OderRespMsg
{
    private String orderNum;
    private Charge charge;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}
