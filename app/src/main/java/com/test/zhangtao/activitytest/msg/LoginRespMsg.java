package com.test.zhangtao.activitytest.msg;

/**
 * Created by zhangtao on 16/11/1.
 */
public class LoginRespMsg<T> extends BaseRespMsg
{
    private String token;
    private T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
