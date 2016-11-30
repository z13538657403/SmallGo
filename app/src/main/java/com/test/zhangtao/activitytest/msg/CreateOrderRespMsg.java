package com.test.zhangtao.activitytest.msg;

/**
 * Created by zhangtao on 16/11/4.
 */
public class CreateOrderRespMsg extends BaseRespMsg
{
    private OderRespMsg data;

    public OderRespMsg getData() {
        return data;
    }

    public void setData(OderRespMsg data) {
        this.data = data;
    }
}
