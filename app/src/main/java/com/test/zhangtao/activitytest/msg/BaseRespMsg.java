package com.test.zhangtao.activitytest.msg;

import java.io.Serializable;

/**
 * Created by zhangtao on 16/11/1.
 */
public class BaseRespMsg implements Serializable
{
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 0;
    public static final String MSG_SUCCESS = "success";

    protected int status = STATUS_SUCCESS;
    protected String message;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
